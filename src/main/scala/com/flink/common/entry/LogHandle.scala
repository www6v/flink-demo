package com.flink.common.entry

import java.util.Date

import com.flink.common.bean._
import com.flink.common.domain.exception.{ExceptionData, ExceptionLog}
import com.flink.common.domain.joinLeave.{InitData, RtcInitOrLeaveLog}
import com.flink.common.domain.operation.{OpertionData, OpertionLog}
import com.flink.common.domain.status.RtcStatusLog
import org.slf4j.Logger
import org.slf4j.LoggerFactory._


class LogHandle {
  val logger: Logger  = getLogger(classOf[LogHandle])

  val MOCK: String = "mock";

  def handleInitLog(rtcClinetLog: RtcInitOrLeaveLog): MonitorRoomBean = {
    val statusType: Integer = rtcClinetLog.getType /// 1 通话开始 2 通话状态 3 通话结束
    if (statusType == Constants.STATUS_TYPE_INIT ||
      statusType == Constants.STATUS_TYPE_LEAVE) {         //  1 通话开始, 3 通话结束

      val rid: String = rtcClinetLog.getRid
      val uid: String = rtcClinetLog.getUid
      val time: Long = rtcClinetLog.getTs // 时间
      val data: InitData = rtcClinetLog.getData

      val aid: String = rtcClinetLog.getAid

      new MonitorRoomBean(aid, rid, uid, statusType, time, data)
    }
    else {
      logger.error("Init" + " error log." +
        "rid: " + rtcClinetLog.getRid +
        "uid: " + rtcClinetLog.getUid +
        "statusType: " + statusType +
        "Ts" + rtcClinetLog.getTs )

      new MonitorRoomBean("mockAid", "mockRid", "mockUid", Constants.STATUS_TYPE_INIT, new Date().getTime, rtcClinetLog.getData)
    }
  }

  def handleStatusLog(rtcClinetLog: RtcStatusLog): MonitorStatusBean = {
    if (rtcClinetLog == null ||
      rtcClinetLog.getData == null ||
      rtcClinetLog.getData.getVideo == null) {
      errorHandleStatus(rtcClinetLog, rtcClinetLog.getType)
      null
    }
    else {
      if (rtcClinetLog.getData.getVideo.getBr == null ||
        rtcClinetLog.getData.getVideo.getLostpre == null) {
        errorHandleStatus(rtcClinetLog, rtcClinetLog.getType)
      }
      else {

        val statusType: Integer = rtcClinetLog.getType /// 1 通话开始 2 通话状态 3 通话结束
        if (statusType == Constants.STATUS_TYPE_STATUS) {

          val sType = rtcClinetLog.getStype /// 流类别  1 发布流 2 订阅流  // 上行、下行
          var delay: Integer = -1
          if (sType.equals(Constants.STREAM_PUB)) {
            delay = rtcClinetLog.getData.getRtt
          }
          if (sType.equals(Constants.STREAM_SUB)) {
            delay = rtcClinetLog.getData.getDelay
          }

          ///
          val time = rtcClinetLog.getTs // 时间
          val uid: String = rtcClinetLog.getUid
          val rid: String = rtcClinetLog.getRid
          val aid:String = rtcClinetLog.getAid;
          val mType:Integer = rtcClinetLog.getMtype;
          val rpc_id:String = rtcClinetLog.getRpc_id
          val sid:String = rtcClinetLog.getSid
          val streamid:String = rtcClinetLog.getStreamid
          ///

          val br = String.valueOf(rtcClinetLog.getData.getVideo.getBr) /// 码率
          val lostPre = String.valueOf(rtcClinetLog.getData.getVideo.getLostpre) /// 丢包率
          val frt = String.valueOf(rtcClinetLog.getData.getVideo.getFrt) /// 发送的帧率

          val pubUserid : String = rtcClinetLog.getData.getUserid;
          val pubStreamid : String = rtcClinetLog.getData.getStreamid;

          new MonitorStatusBean(rid, uid, sType,
            br, lostPre, frt, delay,
            time, aid, mType, rpc_id, sid, streamid,
            pubUserid, pubStreamid
          )
        }
        else {
          errorHandleStatus(rtcClinetLog, statusType)
        }
      }
    }
  }

  def errorHandleStatus(rtcClinetLog: RtcStatusLog, statusType: Integer): MonitorStatusBean = {
    logger.error("Status" + " error log." +
      "rid: " + rtcClinetLog.getRid +
      "uid: " + rtcClinetLog.getUid +
      "statusType: " + statusType +
      "Ts" + rtcClinetLog.getTs)

    new MonitorStatusBean(MOCK + "Rid", MOCK + "uid", 0,
      "0", "0", "0", 0,
      new Date().getTime, MOCK + "aid", 0, MOCK + "rpc_id", MOCK + "sid", MOCK + "streamid",
      MOCK + "pubUserid", MOCK + "pubStreamid"
    )
  }

  def handleAudioStatusLog(rtcClinetLog: RtcStatusLog): MonitorAudioStatusBean = {
    if (rtcClinetLog == null ||
      rtcClinetLog.getData == null ||
      rtcClinetLog.getData.getAudio == null) {
      handleErrorAudioStatus(rtcClinetLog, rtcClinetLog.getType)
    }
    else {
      if (rtcClinetLog.getData.getAudio.getBr == null ||
        rtcClinetLog.getData.getAudio.getLostpre == null) {
        handleErrorAudioStatus(rtcClinetLog, rtcClinetLog.getType)
      }
      else {

        val statusType: Integer = rtcClinetLog.getType /// 1 通话开始 2 通话状态 3 通话结束
        if (statusType == Constants.STATUS_TYPE_STATUS) {

          val sType = rtcClinetLog.getStype /// 流类别  1 发布流 2 订阅流  // 上行、下行

          ///
          val time = rtcClinetLog.getTs // 时间
          val uid: String = rtcClinetLog.getUid
          val rid: String = rtcClinetLog.getRid
          val aid:String = rtcClinetLog.getAid;
          val mType:Integer = rtcClinetLog.getMtype;
          val rpc_id:String = rtcClinetLog.getRpc_id
          val sid:String = rtcClinetLog.getSid
          val streamId:String = rtcClinetLog.getStreamid
          ///

          val br = String.valueOf(rtcClinetLog.getData.getAudio.getBr) /// 码率
          val lostPre = String.valueOf(rtcClinetLog.getData.getAudio.getLostpre) /// 丢包率
          val volume = String.valueOf(rtcClinetLog.getData.getAudio.getVol) ///  音量

          val pubUserid : String = rtcClinetLog.getData.getUserid;
          val pubStreamid : String = rtcClinetLog.getData.getStreamid;

          new MonitorAudioStatusBean(rid, uid, sType,
            br, lostPre,  volume,
            time, aid, mType, rpc_id, sid, streamId,
            pubUserid, pubStreamid
          )
        }
        else {

          handleErrorAudioStatus(rtcClinetLog, statusType)
        }
      }
    }
  }

  def handleErrorAudioStatus(rtcClinetLog: RtcStatusLog, statusType: Integer): MonitorAudioStatusBean = {
    logger.error("AudioStatus " + " error log." +
      "rid: " + rtcClinetLog.getRid +
      "uid: " + rtcClinetLog.getUid +
      "statusType: " + statusType +
      "Ts" + rtcClinetLog.getTs)

    new MonitorAudioStatusBean(MOCK + "rid", MOCK + "uid", 0,
      "0", "0", "0",
      new Date().getTime, MOCK + "aid", 0, MOCK + "rpc_id", MOCK + "sid", MOCK + "streamId",
      MOCK + "pubUserid", MOCK + "pubStreamid"
    )
  }

  def handleResourceStatusLog(rtcClinetLog: RtcStatusLog): MonitorResourceStatusBean = {
    if (rtcClinetLog == null ||
      rtcClinetLog.getData == null ||
      rtcClinetLog.getData.getAudio == null) {
      handleErrorResouceStatus(rtcClinetLog, rtcClinetLog.getType)
    }
    else {
      if (rtcClinetLog.getData.getAudio.getBr == null ||
        rtcClinetLog.getData.getAudio.getLostpre == null) {
        handleErrorResouceStatus(rtcClinetLog, rtcClinetLog.getType)
      }
      else {

        val statusType: Integer = rtcClinetLog.getType /// 1 通话开始 2 通话状态 3 通话结束
        if (statusType == Constants.STATUS_TYPE_STATUS) {

          val sType = rtcClinetLog.getStype /// 流类别  1 发布流 2 订阅流  // 上行、下行

          ///
          val time = rtcClinetLog.getTs // 时间
          val uid: String = rtcClinetLog.getUid
          val rid: String = rtcClinetLog.getRid
          val aid:String = rtcClinetLog.getAid;
          val mType:Integer = rtcClinetLog.getMtype;
          val rpc_id:String = rtcClinetLog.getRpc_id
          val sid:String = rtcClinetLog.getSid
          val streamId:String = rtcClinetLog.getStreamid
          ///

          val cpu = String.valueOf(rtcClinetLog.getData.getCpu) ///
          val memory = String.valueOf(rtcClinetLog.getData.getMemory) ///

          val pubUserid : String = rtcClinetLog.getData.getUserid;
          val pubStreamid : String = rtcClinetLog.getData.getStreamid;

          new MonitorResourceStatusBean(rid, uid, sType,
            cpu, memory,
            time, aid, mType, rpc_id, sid, streamId,
            pubUserid, pubStreamid
          )
        }
        else {
          handleErrorResouceStatus(rtcClinetLog, statusType)
        }
      }
    }
  }

  def handleErrorResouceStatus(rtcClinetLog: RtcStatusLog, statusType: Integer): MonitorResourceStatusBean = {
    logger.error("ResourceStatus " + " error log." +
      "rid: " + rtcClinetLog.getRid +
      "uid: " + rtcClinetLog.getUid +
      "statusType: " + statusType +
      "Ts" + rtcClinetLog.getTs)

    new MonitorResourceStatusBean(MOCK + "rid", MOCK + "uid", 0,
      "0", "0",
      new Date().getTime, MOCK + "aid", 0, MOCK + "rpc_id", MOCK + "sid", MOCK + "streamId",
      MOCK + "pubUserid", MOCK + "pubStreamid"
    )
  }

  def handleOpertionLog(rtcOpertionLog: OpertionLog): MonitorOpertionBean = {
    val statusType: Integer = rtcOpertionLog.getType
    if (statusType == Constants.STATUS_TYPE_OPERATION) {

      val aid: String = rtcOpertionLog.getAid

      val rid: String = rtcOpertionLog.getRid
      val uid: String = rtcOpertionLog.getUid
      val time: Long = rtcOpertionLog.getTs // 时间
      val data: OpertionData = rtcOpertionLog.getData

      new MonitorOpertionBean(aid, rid, uid, statusType, time, data)
    }
    else {
      logger.error("Opertion " + " error log." +
        "aid: " + rtcOpertionLog.getAid +
        "rid: " + rtcOpertionLog.getRid +
        "uid: " + rtcOpertionLog.getUid +
        "statusType: " + statusType +
        "Ts" + rtcOpertionLog.getTs )

      new MonitorOpertionBean(MOCK+"aid", MOCK+"rid", MOCK+"uid", statusType, new Date().getTime, rtcOpertionLog.getData)
    }
  }

  def handleExceptionLog(exceptionLog: ExceptionLog): MonitorExceptionBean = {
    val statusType: Integer = exceptionLog.getType
    if (statusType == Constants.STATUS_TYPE_EXCEPTION) {

      val aid: String = exceptionLog.getAid

      val rid: String = exceptionLog.getRid
      val uid: String = exceptionLog.getUid
      val time: Long = exceptionLog.getTs // 时间
      val data: ExceptionData = exceptionLog.getData

      new MonitorExceptionBean(aid, rid, uid, statusType, time, data)
    }
    else {
      logger.error("Exception " + " error log." +
        "aid: " + exceptionLog.getAid +
        "rid: " + exceptionLog.getRid +
        "uid: " + exceptionLog.getUid +
        "statusType: " + statusType +
        "Ts" + exceptionLog.getTs )

      new MonitorExceptionBean(MOCK+"aid", MOCK+"rid", MOCK+"uid", statusType, new Date().getTime, exceptionLog.getData)
    }
  }
}
