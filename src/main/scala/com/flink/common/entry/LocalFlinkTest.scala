package com.flink.common.entry

import com.alibaba.fastjson.{TypeReference, JSON}
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
            val rtcClinetLog: RtcClinetLog = JSON.parseObject(x._2, new TypeReference[RtcClinetLog]() {});

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
                val time = rtcClinetLog.getTs
                val br = String.valueOf(rtcClinetLog.getData.getVideo.getBr)
                val lostpre = String.valueOf(rtcClinetLog.getData.getVideo.getLostpre)

                new AdlogBean(rtcClinetLog.getUid, br, lostpre, time, StatisticalIndic(1))
              }
            }

//            val datas = x._2.split(",")
//            val statdate = datas(0).substring(0, 10) //日期
//            val hour = datas(0).substring(11, 13) //hour
//    //        val plan = datas(25)
//            val plan = datas(1)
//            if (plan.nonEmpty) {
//              new AdlogBean(plan, statdate, hour, StatisticalIndic(1))
//            } else null

          }
      }
      .filter { x =>
        x != null
      }
      .keyBy(_.key) //按key分组，可以把key相同的发往同一个slot处理
      .flatMap(new AdlogPVRichFlatMapFunction)

    result.setParallelism(1).writeAsText("/home/wei/flink/result/result.txt", WriteMode.OVERWRITE)


    //operate state。用于写hbase是吧恢复
//    result.addSink(new StateRecoverySinkCheckpointFunc(50))
//    result.addSink(new SystemPrintSink)
    //result.addSink(new HbaseReportSink)

    env.execute("rtc-log")
  }

}
