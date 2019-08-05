package com.flink.common.entry

import com.alibaba.fastjson.{JSONException, TypeReference, JSON}
import com.flink.common.domain.RtcClinetLog
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer08

import scala.collection.JavaConversions._

import com.flink.common.bean.{AdlogBean, StatisticalIndic}
import com.flink.common.richf.{
  AdlogPVRichFlatMapFunction,
  AdlogPVRichMapFunction
}
import com.flink.common.sink.{
  HbaseReportSink,
  StateRecoverySinkCheckpointFunc,
  SystemPrintSink
}
import com.flink.common.bean.AdlogBean

object LocalFlinkTest {
//  val cp = "file:///C:\\Users\\Master\\Desktop\\rocksdbcheckpoint"
  val cp = "/home/wei/flink/rocksdbcheckpoint"

  def main(args: Array[String]): Unit = {
    println("rtc log  ")
    val kafkasource = new FlinkKafkaConsumer08[(String, String)](
      TOPIC.split(",").toList,
      new TopicMessageDeserialize(),
      getKafkaParam(BROKER))
    kafkasource.setCommitOffsetsOnCheckpoints(true)
    kafkasource.setStartFromLatest() //不加这个默认是从上次消费
    val env = getFlinkEnv(cp,60000) // 1 min
    val result = env
      .addSource(kafkasource)
      .filter { x => !x.equals("")  }
      .map { x => {
            try {
              val rtcClinetLog: RtcClinetLog = JSON.parseObject(x._2, new TypeReference[RtcClinetLog]() {});
              handleLog(rtcClinetLog)
            }catch {
              case ex: Exception => {println("捕获了异常：" + ex); null}
            }
          }
      }
      .filter { x =>
        x != null
      }
      .keyBy(_.key) //按key分组，可以把key相同的发往同一个slot处理
      .flatMap(new AdlogPVRichFlatMapFunction)

    result.setParallelism(1).writeAsText("/home/wei/flink/result/result.txt", WriteMode.OVERWRITE)


    //operate state
//    result.addSink(new StateRecoverySinkCheckpointFunc(50))
    result.addSink(new SystemPrintSink)
//    result.addSink(new HbaseReportSink)

    env.execute("rtc-log")
  }

  def handleLog(rtcClinetLog: RtcClinetLog): AdlogBean = {
    if (rtcClinetLog == null ||
      rtcClinetLog.getData == null ||
      rtcClinetLog.getData.getVideo == null) {
      null
    }
    else {
      if (rtcClinetLog.getData.getVideo.getBr == null ||
        rtcClinetLog.getData.getVideo.getLostpre == null) {
        null
      }
      else {

        val uid: String = rtcClinetLog.getUid
        val stype = rtcClinetLog.getStype /// 流类别 1 发布流 2 订阅流

        var delay: Integer = -1
        if (stype.equals(1)) {
          /// 1 发布流
          delay = rtcClinetLog.getData.getRtt
        }
        if (stype.equals(2)) {
          delay = rtcClinetLog.getData.getDelay
        }

        val time = rtcClinetLog.getTs // 时间

        val br = String.valueOf(rtcClinetLog.getData.getVideo.getBr) /// 码率
        val lostpre = String.valueOf(rtcClinetLog.getData.getVideo.getLostpre) /// 丢包率
        val frt = String.valueOf(rtcClinetLog.getData.getVideo.getFrt) /// 发送的帧率

        new AdlogBean(uid, stype,
          br, lostpre, frt,
          delay, time, StatisticalIndic(1))
      }
    }
  }
}
