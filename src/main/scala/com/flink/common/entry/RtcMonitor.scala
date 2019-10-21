package com.flink.common.entry

import com.alibaba.fastjson.{JSON, TypeReference}
import com.flink.common.bean._
import com.flink.common.domain.exception.{ExceptionData, ExceptionLog}
import com.flink.common.domain.joinLeave.{InitData, RtcInitOrLeaveLog}
import com.flink.common.domain.operation.{OpertionData, OpertionLog}
import com.flink.common.domain.status.RtcStatusLog
import com.flink.common.richf.{ExceptionRichFlatMapFunction, OperationRichFlatMapFunction, InitRichFlatMapFunction, StatusRichFlatMapFunction}
import com.flink.common.sink._

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer08
import org.slf4j.{LoggerFactory, Logger}

import scala.collection.JavaConversions._


object RtcMonitor   {
  //  val cp = "file:///C:\\Users\\Master\\Desktop\\rocksdbcheckpoint"
  val cp = "/home/wei/flink/rocksdbcheckpoint"

  def main(args: Array[String]): Unit = {
    println("rtc log")

    val kafkaSourceJoinLeave: FlinkKafkaConsumer08[(String, String)] = getKafkaSourceJoinLeave
    val kafkaProcess: FlinkKafkaConsumer08[(String, String)] = getKafkaSourceProcess
    val kafkaOperation: FlinkKafkaConsumer08[(String, String)] = getKafkaSourceOperation
    val kafkaException: FlinkKafkaConsumer08[(String, String)] = getKafkaSourceException

    val env = getFlinkEnv(cp, 60000) // 1 min

    handleJoinLeave(env,kafkaSourceJoinLeave)
    handleStatus(env,kafkaProcess)
    handleOperation(env,kafkaOperation)
    handleException(env, kafkaException);

    env.execute("rtc-log")
    //    env1.execute("rtc-log-1")
  }

  def handleJoinLeave(env:StreamExecutionEnvironment, kafkasource: FlinkKafkaConsumer08[(String, String)]): Unit = {
    //    val env = getFlinkEnv(cp, 60000) // 1 min
    val result = env
      .addSource(kafkasource)
      .filter { x => !x.equals("") }
      .filter { x => !x._2.contains("ios") }  /// ios
      .filter { x => !x._2.contains("iphone") }  /// iphone
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
      .flatMap(new InitRichFlatMapFunction)

    //    result.setParallelism(1).writeAsText("/home/wei/flink/result/resultInit.txt", WriteMode.OVERWRITE)

    result.addSink(new JoinLeaveDBSink)
    //    result.addSink(new StateRecoverySinkCheckpointFunc(50))
    //    result.addSink(new HbaseReportSink)
    //    env
  }

  def handleStatus(env:StreamExecutionEnvironment, kafkasource: FlinkKafkaConsumer08[(String, String)]): Unit = {
    //    val env = getFlinkEnv(cp, 60000) // 1 min
    val result = env
      .addSource(kafkasource)
      .filter { x => !x.equals("") }
      .filter {  x => {
        val body = x._2
        val i: Int = body.indexOf("rpc_id")
        val length: Int = "rpc_id\":\"".length

        val index: Int = i + length

        val substring: String = body.substring(index, index + 10)
        //        System.out.println(substring);
        if (substring.contains("android") || substring.contains("ios") || substring.contains("win")) {
          true
        }
        else {
          false
        }
      }}
      .filter { x => !x._2.contains("ios") }  /// ios
      .filter { x => !x._2.contains("iphone") }  /// iphone
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
      .flatMap(new StatusRichFlatMapFunction)

    result.addSink(new StatusPrometheusSink).setParallelism(1) // new MonitorPrintSink
    result.addSink(new StatusOpenFalconSink).setParallelism(1)
    //    result.addSink(new StateRecoverySinkCheckpointFunc(50))
    //    result.addSink(new HbaseReportSink)
    //    env
  }

  def handleOperation(env: StreamExecutionEnvironment, kafkaOperation: FlinkKafkaConsumer08[(String, String)]) = {
    //    val env = getFlinkEnv(cp, 60000) // 1 min
    val result = env
      .addSource(kafkaOperation)
      .filter { x => !x.equals("") }
      .filter { x => !x._2.contains("ios") }  /// ios
      .filter { x => !x._2.contains("iphone") }  /// iphone
      .map { x => {
      try {
        val opertionLog: OpertionLog = JSON.parseObject(x._2, new TypeReference[OpertionLog]() {});
        handleOpertionLog(opertionLog)
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
      .keyBy(_.key)
      .flatMap(new OperationRichFlatMapFunction)

    //    result.setParallelism(1).writeAsText("/home/wei/flink/result/resultInit.txt", WriteMode.OVERWRITE)

    result.addSink(new OperationSink)
  }

  def handleException(env: StreamExecutionEnvironment, kafkaException: FlinkKafkaConsumer08[(String, String)]) = {
    //    val env = getFlinkEnv(cp, 60000) // 1 min
    val result = env
      .addSource(kafkaException)
      .filter { x => !x.equals("") }
      .filter { x => !x._2.contains("ios") }  /// ios
      .filter { x => !x._2.contains("iphone") }  /// iphone
      .map { x => {
      try {
        val exceptionLog: ExceptionLog = JSON.parseObject(x._2, new TypeReference[ExceptionLog]() {});
        handleExceptionLog(exceptionLog)
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
      .keyBy(_.key)
      .flatMap(new ExceptionRichFlatMapFunction)

    result.addSink(new ExceptionSink)
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

  def getKafkaSourceOperation: FlinkKafkaConsumer08[(String, String)] = {
    val kafkasource = new FlinkKafkaConsumer08[(String, String)](
      TOPIC_OPERATION.split(",").toList,
      new TopicMessageDeserialize(),
      getKafkaParam(BROKER))
    kafkasource.setCommitOffsetsOnCheckpoints(true)
    kafkasource.setStartFromLatest() //不加这个默认是从上次消费
    kafkasource
  }

  def getKafkaSourceException: FlinkKafkaConsumer08[(String, String)] = {
    val kafkasource = new FlinkKafkaConsumer08[(String, String)](
      TOPIC_EXCEPTION.split(",").toList,
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
        if (rtcType == Constants.STATUS_TYPE_STATUS) {
          val uid: String = rtcClinetLog.getUid
          val rid: String = rtcClinetLog.getRid
          val sType = rtcClinetLog.getStype /// 流类别 1 发布流 2 订阅流

          var delay: Integer = -1
          if (sType.equals(Constants.STREAM_PUB)) {
            delay = rtcClinetLog.getData.getRtt
          }
          if (sType.equals(Constants.STREAM_SUB)) {
            delay = rtcClinetLog.getData.getDelay
          }

          val time = rtcClinetLog.getTs // 时间

          ///
          val aid:String = rtcClinetLog.getAid;
          val mType:Integer = rtcClinetLog.getMtype;
          val rpc_id:String = rtcClinetLog.getRpc_id
          val sid:String = rtcClinetLog.getSid
          val streamid:String = rtcClinetLog.getStreamid
          ///

          val br = String.valueOf(rtcClinetLog.getData.getVideo.getBr) /// 码率
          val lostPre = String.valueOf(rtcClinetLog.getData.getVideo.getLostpre) /// 丢包率
          val frt = String.valueOf(rtcClinetLog.getData.getVideo.getFrt) /// 发送的帧率

          new MonitorStatusBean(rid, uid, sType,
            br, lostPre, frt,
            delay, time,
            aid, mType, rpc_id,sid,streamid
          )
        }
        else {
          null
        }
      }
    }
  }

  def handleOpertionLog(rtcOpertionLog: OpertionLog): MonitorOpertionBean = {
    val statusType: Integer = rtcOpertionLog.getType
    if (statusType == Constants.STATUS_TYPE_OPERATION) {

      val rid: String = rtcOpertionLog.getRid
      val uid: String = rtcOpertionLog.getUid
      val time: Long = rtcOpertionLog.getTs // 时间
      val data: OpertionData = rtcOpertionLog.getData

      new MonitorOpertionBean(rid, uid, statusType, time, data)
    }
    else {
      null
    }
  }

  def handleExceptionLog(exceptionLog: ExceptionLog): MonitorExceptionBean = {
    val statusType: Integer = exceptionLog.getType
    if (statusType == Constants.STATUS_TYPE_EXCEPTION) {

      val rid: String = exceptionLog.getRid
      val uid: String = exceptionLog.getUid
      val time: Long = exceptionLog.getTs // 时间
      val data: ExceptionData = exceptionLog.getData

      new MonitorExceptionBean(rid, uid, statusType, time, data)
    }
    else {
      null
    }
  }
}
