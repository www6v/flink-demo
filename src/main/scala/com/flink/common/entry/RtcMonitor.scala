package com.flink.common.entry

import com.alibaba.fastjson.{JSON, TypeReference}
import com.flink.common.bean.{MonitorRoomBean, MonitorBean, AdlogBean, StatisticalIndic}
import com.flink.common.domain.RtcClinetLog
import com.flink.common.richf.{RtcMonitorInitRichFlatMapFunction, RtcMonitorRichFlatMapFunction, AdlogPVRichFlatMapFunction}
import com.flink.common.sink.{MonitorInitPrintSink, MonitorPrintSink, SystemPrintSink}
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer08

import scala.collection.JavaConversions._

object RtcMonitor {
//  val cp = "file:///C:\\Users\\Master\\Desktop\\rocksdbcheckpoint"
  val cp = "/home/wei/flink/rocksdbcheckpoint"

  def main(args: Array[String]): Unit = {
    println("rtc log  ")

    val kafkaSource: FlinkKafkaConsumer08[(String, String)] = getKafkaSource

    val env = getFlinkEnv(cp, 60000) // 1 min
    handleCallInitStats(env,kafkaSource)
//    handleCallStats(env,kafkaSource)

    env.execute("rtc-log")
//    env1.execute("rtc-log-1")
  }

  def handleCallInitStats(env:StreamExecutionEnvironment,kafkasource: FlinkKafkaConsumer08[(String, String)]): Unit = {
    //    val env = getFlinkEnv(cp, 60000) // 1 min
    val result = env
      .addSource(kafkasource)
      .filter { x => !x.equals("") }
      .map { x => {
        try {
          val rtcClinetLog: RtcClinetLog = JSON.parseObject(x._2, new TypeReference[RtcClinetLog]() {});
          handleInitLog(rtcClinetLog)
        } catch {
          case ex: Exception => {
            println("捕获了异常：" + ex);
            null
          }
        }
      }
      }
      .filter { x =>
        x != null
      }
      .keyBy(_.key) //按key分组，可以把key相同的发往同一个slot处理
      .flatMap(new RtcMonitorInitRichFlatMapFunction)

    result.setParallelism(1).writeAsText("/home/wei/flink/result/resultInit.txt", WriteMode.OVERWRITE)

    result.addSink(new MonitorInitPrintSink)
    //    result.addSink(new StateRecoverySinkCheckpointFunc(50))
    //    result.addSink(new HbaseReportSink)
    //    env
  }

  def handleCallStats(env:StreamExecutionEnvironment,kafkasource: FlinkKafkaConsumer08[(String, String)]): Unit = {
//    val env = getFlinkEnv(cp, 60000) // 1 min
    val result = env
        .addSource(kafkasource)
        .filter { x => !x.equals("") }
        .map { x => {
          try {
            val rtcClinetLog: RtcClinetLog = JSON.parseObject(x._2, new TypeReference[RtcClinetLog]() {});
            handleLog(rtcClinetLog)
          } catch {
            case ex: Exception => {
              println("捕获了异常：" + ex);
              null
            }
          }
        }
        }
        .filter { x =>
          x != null
        }
        .keyBy(_.key) //按key分组，可以把key相同的发往同一个slot处理
        .flatMap(new RtcMonitorRichFlatMapFunction)

    result.setParallelism(1).writeAsText("/home/wei/flink/result/result.txt", WriteMode.OVERWRITE)

    result.addSink(new MonitorPrintSink)
    //    result.addSink(new StateRecoverySinkCheckpointFunc(50))
    //    result.addSink(new HbaseReportSink)
//    env
  }

  def getKafkaSource: FlinkKafkaConsumer08[(String, String)] = {
    val kafkasource = new FlinkKafkaConsumer08[(String, String)](
      TOPIC.split(",").toList,
      new TopicMessageDeserialize(),
      getKafkaParam(BROKER))
    kafkasource.setCommitOffsetsOnCheckpoints(true)
    kafkasource.setStartFromLatest() //不加这个默认是从上次消费
    kafkasource
  }

  def handleInitLog(rtcClinetLog: RtcClinetLog): MonitorRoomBean = {
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

        val statusType: Integer = rtcClinetLog.getType /// 1 通话开始 2 通话状态 3 通话结束
        if (statusType == Constants.STATUS_TYPE_INIT ||
          statusType == Constants.STATUS_TYPE_LEAVE) { //  1 通话开始

          val rid: String = rtcClinetLog.getRid
          val uid: String = rtcClinetLog.getUid
          val time = rtcClinetLog.getTs // 时间

          new MonitorRoomBean(rid, uid, statusType, time)
        }
        else {
          null
        }
      }
    }
  }

  def handleLog(rtcClinetLog: RtcClinetLog): MonitorBean = {
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

        val rtcType: Integer = rtcClinetLog.getType /// 1 通话开始 2 通话状态 3 通话结束
        if (rtcType == 2) {
          val uid: String = rtcClinetLog.getUid
          val rid: String = rtcClinetLog.getRid
          val sType = rtcClinetLog.getStype /// 流类别 1 发布流 2 订阅流

          var delay: Integer = -1
          if (sType.equals(1)) {
            /// 1 发布流
            delay = rtcClinetLog.getData.getRtt
          }
          if (sType.equals(2)) {
            delay = rtcClinetLog.getData.getDelay
          }

          val time = rtcClinetLog.getTs // 时间

          val br = String.valueOf(rtcClinetLog.getData.getVideo.getBr) /// 码率
          val lostPre = String.valueOf(rtcClinetLog.getData.getVideo.getLostpre) /// 丢包率
          val frt = String.valueOf(rtcClinetLog.getData.getVideo.getFrt) /// 发送的帧率

          new MonitorBean(rid, uid, sType,
            br, lostPre, frt,
            delay, time, StatisticalIndic(1))
        }
        else {
          null
        }
      }
    }
  }
}
