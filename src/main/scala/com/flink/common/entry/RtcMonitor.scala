package com.flink.common.entry

import com.alibaba.fastjson.{JSON, TypeReference}
import com.flink.common.domain.exception.{ ExceptionLog}
import com.flink.common.domain.joinLeave.{ RtcInitOrLeaveLog}
import com.flink.common.domain.operation.{ OpertionLog}
import com.flink.common.domain.status.RtcStatusLog
import com.flink.common.richf._
import com.flink.common.sink._

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer08
import org.slf4j.LoggerFactory.getLogger
import org.slf4j.{LoggerFactory, Logger}

import scala.collection.JavaConversions._
import org.slf4j.Logger

object RtcMonitor  extends LogHandle {
  //  val cp = "file:///C:\\Users\\Master\\Desktop\\rocksdbcheckpoint"
  val cp = "/home/wei/flink/rocksdbcheckpoint"

//  Logger LOG = LoggerFactory.getLogger(RtcMonitor.class)
  val loggerRtcMonitor: Logger  = getLogger("RtcMonitor")

  def main(args: Array[String]): Unit = {
    loggerRtcMonitor.info("rtc log")

    val kafkaSourceJoinLeave: FlinkKafkaConsumer08[(String, String)] = getKafkaSourceJoinLeave
    val kafkaProcess: FlinkKafkaConsumer08[(String, String)] = getKafkaSourceProcess
    val kafkaOperation: FlinkKafkaConsumer08[(String, String)] = getKafkaSourceOperation
    val kafkaException: FlinkKafkaConsumer08[(String, String)] = getKafkaSourceException

    val env = getFlinkEnv(cp, 60000) // 1 min

    handleJoinLeave(env,kafkaSourceJoinLeave)

    handleStatus(env,kafkaProcess)
    handleAudioStatus(env,kafkaProcess)
    handleResourceStatus(env,kafkaProcess)

    handleOperation(env,kafkaOperation)
    handleException(env, kafkaException);

    env.execute("rtc-log")
    //    env1.execute("rtc-log-1")
  }

  /////
  def handleJoinLeave(env:StreamExecutionEnvironment, kafkasource: FlinkKafkaConsumer08[(String, String)]): Unit = {
    //    val env = getFlinkEnv(cp, 60000) // 1 min
    val result = env
      .addSource(kafkasource)
      .filter { x => !x.equals("") }
      .map { x => {
      try {
        //          val rtcClinetLog: RtcInitLog = JSON.parseObject(x._2, new TypeReference[RtcInitLog]() {});
        val rtcClinetLog: RtcInitOrLeaveLog = JSON.parseObject(x._2, new TypeReference[RtcInitOrLeaveLog]() {});
        handleInitLog(rtcClinetLog)
      } catch {
        case ex: Exception => {
          loggerRtcMonitor.error( "捕获了异常：" + ex.toString );
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

    result.addSink(new JoinLeaveDBSink)
  }

  def handleStatus(env:StreamExecutionEnvironment, kafkasource: FlinkKafkaConsumer08[(String, String)]): Unit = {
    //    val env = getFlinkEnv(cp, 60000) // 1 min
    val result = env
      .addSource(kafkasource)
      .filter { x => !x.equals("") }
      .filter {  x => {
        filterStatus(x)
      }}
      .map { x => {
      try {
        val rtcClinetLog: RtcStatusLog = JSON.parseObject(x._2, new TypeReference[RtcStatusLog]() {});
        handleStatusLog(rtcClinetLog)
      } catch {
        case ex: Exception => {
          loggerRtcMonitor.error( "捕获了异常：" + ex.toString );
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

//    result.addSink(new StatusPrometheusSink).setParallelism(1) // new MonitorPrintSink
    result.addSink(new StatusOpenFalconSink).setParallelism(1)
  }

  def handleAudioStatus(env:StreamExecutionEnvironment, kafkasource: FlinkKafkaConsumer08[(String, String)]): Unit = {
    //    val env = getFlinkEnv(cp, 60000) // 1 min
    val result = env
      .addSource(kafkasource)
      .filter { x => !x.equals("") }
      .filter {  x => {
        filterStatus(x)

      }}
      .map { x => {
      try {
        val rtcClinetLog: RtcStatusLog = JSON.parseObject(x._2, new TypeReference[RtcStatusLog]() {});
        handleAudioStatusLog(rtcClinetLog)
      } catch {
        case ex: Exception => {
          loggerRtcMonitor.error( "捕获了异常：" + ex.toString );
          null
        }
      }
    }
    }
      .filter { x =>
        x != null
      }
      .keyBy(_.key) //按key分组，可以把key相同的发往同一个slot处理
      .flatMap(new StatusAudioRichFlatMapFunction)

    //    result.addSink(new StatusPrometheusSink).setParallelism(1) // new MonitorPrintSink
    result.addSink(new StatusAudioOpenFalconSink).setParallelism(1)
  }

  def handleResourceStatus(env:StreamExecutionEnvironment, kafkasource: FlinkKafkaConsumer08[(String, String)]): Unit = {
    val result = env
      .addSource(kafkasource)
      .filter { x => !x.equals("") }
      .filter {  x => {
        filterStatus(x)

      }}
      .map { x => {
      try {
        val rtcClinetLog: RtcStatusLog = JSON.parseObject(x._2, new TypeReference[RtcStatusLog]() {});
        handleResourceStatusLog(rtcClinetLog)
      } catch {
        case ex: Exception => {
          loggerRtcMonitor.error( "捕获了异常：" + ex.toString );
          null
        }
      }
    }
    }
      .filter { x =>
        x != null
      }
      .keyBy(_.key) //按key分组，可以把key相同的发往同一个slot处理
      .flatMap(new StatusResourceRichFlatMapFunction)

    result.addSink(new StatusResourceOpenFalconSink).setParallelism(1)
  }



  def handleOperation(env: StreamExecutionEnvironment, kafkaOperation: FlinkKafkaConsumer08[(String, String)]) = {
    //    val env = getFlinkEnv(cp, 60000) // 1 min
    val result = env
      .addSource(kafkaOperation)
      .filter { x => !x.equals("") }
      .map { x => {
      try {
        val opertionLog: OpertionLog = JSON.parseObject(x._2, new TypeReference[OpertionLog]() {});
        handleOpertionLog(opertionLog)
      } catch {
        case ex: Exception => {
          loggerRtcMonitor.error( "捕获了异常：" + ex.toString );
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

    result.addSink(new OperationSink)
  }

  def handleException(env: StreamExecutionEnvironment, kafkaException: FlinkKafkaConsumer08[(String, String)]) = {
    //    val env = getFlinkEnv(cp, 60000) // 1 min
    val result = env
      .addSource(kafkaException)
      .filter { x => !x.equals("") }
      .map { x => {
      try {
        val exceptionLog: ExceptionLog = JSON.parseObject(x._2, new TypeReference[ExceptionLog]() {});
        handleExceptionLog(exceptionLog)
      } catch {
        case ex: Exception => {
          loggerRtcMonitor.error( "捕获了异常：" + ex.toString );
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

  /////

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

  ////

  def filterStatus(x: (String, String)): Boolean = {
    val body = x._2
    val i: Int = body.indexOf("rpc_id")
    val length: Int = "rpc_id\":\"".length

    val index: Int = i + length

    val substring: String = body.substring(index, index + 10)
    if (substring.contains("android") ||
      substring.contains("ios") ||
      substring.contains("win") ||
      substring.contains("web")) {
      true
    }
    else {
      false
    }
  }
}
