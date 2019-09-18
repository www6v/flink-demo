package com.flink.common.entry

import com.alibaba.fastjson.{JSON, TypeReference}
import com.flink.common.bean.{MonitorRoomBean, MonitorStatusBean, AdlogBean, StatisticalIndic}
import com.flink.common.domain._
import com.flink.common.richf.{CallInitRichFlatMapFunction, CallStatusRichFlatMapFunction, AdlogPVRichFlatMapFunction}
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

    val kafkaSourceJoinLeave: FlinkKafkaConsumer08[(String, String)] = getKafkaSourceJoinLeave
    val kafkaProcess: FlinkKafkaConsumer08[(String, String)] = getKafkaSourceProcess


    val env = getFlinkEnv(cp, 60000) // 1 min

//    handleCallInitStats(env,kafkaSourceJoinLeave)
    handleCallStats(env,kafkaProcess)

    env.execute("rtc-log")
//    env1.execute("rtc-log-1")
  }

  def handleCallInitStats(env:StreamExecutionEnvironment,kafkasource: FlinkKafkaConsumer08[(String, String)]): Unit = {
    //    val env = getFlinkEnv(cp, 60000) // 1 min
    val result = env
      .addSource(kafkasource)
//      .filter { x => !x.equals("") }  /// fix
      .map { x => {
        try {
//          val rtcClinetLog: RtcInitLog = JSON.parseObject(x._2, new TypeReference[RtcInitLog]() {});
          val rtcClinetLog: RtcInitOrLeaveLog = JSON.parseObject(x._2, new TypeReference[RtcInitOrLeaveLog]() {});
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
      .flatMap(new CallInitRichFlatMapFunction)

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
            val rtcClinetLog: RtcStatusLog = JSON.parseObject(x._2, new TypeReference[RtcStatusLog]() {});
            handleStatusLog(rtcClinetLog)
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
        .flatMap(new CallStatusRichFlatMapFunction)

    result.setParallelism(1).writeAsText("/home/wei/flink/result/result.txt", WriteMode.OVERWRITE)

    result.addSink(new MonitorPrintSink)
    //    result.addSink(new StateRecoverySinkCheckpointFunc(50))
    //    result.addSink(new HbaseReportSink)
//    env
  }

  def getKafkaSourceJoinLeave: FlinkKafkaConsumer08[(String, String)] = {
    val kafkasource = new FlinkKafkaConsumer08[(String, String)](
      TOPIC_JOIN_LEAVE.split(",").toList,
      new TopicMessageDeserialize(),
      getKafkaParam(BROKER))
    kafkasource.setCommitOffsetsOnCheckpoints(true)
    kafkasource.setStartFromLatest() //不加这个默认是从上次消费
    kafkasource
  }

  def getKafkaSourceProcess: FlinkKafkaConsumer08[(String, String)] = {
    val kafkasource = new FlinkKafkaConsumer08[(String, String)](
      TOPIC_Process.split(",").toList,
      new TopicMessageDeserialize(),
      getKafkaParam(BROKER))
    kafkasource.setCommitOffsetsOnCheckpoints(true)
    kafkasource.setStartFromLatest() //不加这个默认是从上次消费
    kafkasource
  }

  def handleInitLog(rtcClinetLog: RtcInitOrLeaveLog): MonitorRoomBean = {
//    if (rtcClinetLog == null ||
//      rtcClinetLog.getData == null  // leave
//    ) {
//      null
//    }
//    else {
          val statusType: Integer = rtcClinetLog.getType /// 1 通话开始 2 通话状态 3 通话结束
          if (statusType == Constants.STATUS_TYPE_INIT ||
            statusType == Constants.STATUS_TYPE_LEAVE) {         //  1 通话开始, 3 通话结束

            val rid: String = rtcClinetLog.getRid
            val uid: String = rtcClinetLog.getUid
            val time: Long = rtcClinetLog.getTs // 时间
            val data: InitData = rtcClinetLog.getData

//            var data: Data = null
//            if(statusType == Constants.STATUS_TYPE_INIT){
//              data = rtcClinetLog.asInstanceOf[RtcInitLog].getData
//            }
//            if(statusType == Constants.STATUS_TYPE_LEAVE){
//              data = rtcClinetLog.asInstanceOf[RtcLeaveLog].getData
//            }

            new MonitorRoomBean(rid, uid, statusType, time, data)
          }
          else {
            null
          }

//    }
  }

  def handleStatusLog(rtcClinetLog: RtcStatusLog): MonitorStatusBean = {
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

          new MonitorStatusBean(rid, uid, sType,
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
