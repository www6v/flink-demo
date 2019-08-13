package com.flink.common.richf

import com.flink.common.bean.{MonitorRoomBean, MonitorBean}
import com.flink.common.entry.Constants
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{ValueStateDescriptor, ValueState}
import org.apache.flink.configuration.Configuration
import org.apache.flink.util.Collector;

class RtcMonitorInitRichFlatMapFunction
  extends RichFlatMapFunction[MonitorRoomBean, (String,  String, Long, Long, Long, Boolean , Integer,Integer,Integer)]
{
  private var currentUserCount: ValueState[Integer] = _
  private var accumulationUserCount: ValueState[Integer] = _
  private var peekUserCount: ValueState[Integer] = _

  private var startTimeOfRoom: ValueState[Long] = _
  private var endTimeOfRoom: ValueState[Long] = _

  private var roomStatus: ValueState[Boolean] = _
//  private var metric:(String,  String, Long, Integer) = _

  override def open(parameters: Configuration): Unit = {
    val userCountDescriptor = new ValueStateDescriptor[Integer]("currentUserCount", classOf[Integer], 0)  /// 要设置默认值0
    currentUserCount = getRuntimeContext.getState[Integer](userCountDescriptor)

    val accumulationUserCountDescriptor = new ValueStateDescriptor[Integer]("accumulationUserCount", classOf[Integer], 0)  /// 要设置默认值0
    accumulationUserCount = getRuntimeContext.getState[Integer](accumulationUserCountDescriptor)

    val peekUserCountDescriptor = new ValueStateDescriptor[Integer]("peekUserCount", classOf[Integer], 0)  /// 要设置默认值0
    peekUserCount = getRuntimeContext.getState[Integer](peekUserCountDescriptor)

    val startTimeOfRoomDescriptor = new ValueStateDescriptor[Long]("startTimeOfRoom", classOf[Long], 0)  /// 要设置默认值0
    startTimeOfRoom = getRuntimeContext.getState[Long](startTimeOfRoomDescriptor)

    val endTimeOfRoomDescriptor = new ValueStateDescriptor[Long]("endTimeOfRoom", classOf[Long], 0)  /// 要设置默认值0
    endTimeOfRoom = getRuntimeContext.getState[Long](endTimeOfRoomDescriptor)

    val roomStatusDescriptor = new ValueStateDescriptor[Boolean]("endTimeOfRoom", classOf[Boolean], false)  /// 要设置默认值true
    roomStatus = getRuntimeContext.getState[Boolean](roomStatusDescriptor)
  }

  override def flatMap(value: MonitorRoomBean, out: Collector[(String,  String, Long, Long, Long, Boolean, Integer,Integer,Integer)]): Unit = {
    var currentUserAmount = currentUserCount.value
    var accumulationUserAmount = accumulationUserCount.value
    var peekUserAmount = peekUserCount.value
    var startTime = startTimeOfRoom.value
    var endTime = endTimeOfRoom.value
    var roomState = roomStatus.value

    val roomId: String = value.roomId
    val userId: String = value.userId
    val time: Long = value.time

    if (value.statusType == Constants.STATUS_TYPE_INIT) {
      println("roomId", roomId , "userId", userId, "join")
      currentUserAmount += 1

      if(startTime == 0) {  /// init
        startTime = value.time
      }
      if( value.time < startTime  ) {
        startTime = value.time
      }
    }
    if ( value.statusType == Constants.STATUS_TYPE_LEAVE) {
      println("roomId", roomId, "userId", userId, "leave")
      currentUserAmount -= 1

      if(endTime == 0) {  /// init
        endTime = value.time
      }
      if( value.time > endTime  ) {
        endTime = value.time
      }
    }
    if ( currentUserAmount > peekUserAmount ) {
      peekUserAmount = currentUserAmount
    }
    accumulationUserAmount += 1
    if (currentUserAmount != 0) {  /// 还有用户， 房间不空
      roomState = true
    }else {
      roomState = false
    }

    //    metric = (roomId, userId, time, currentUserAmount)
    out.collect((roomId, userId, time, startTime, endTime, roomState, currentUserAmount,peekUserAmount ,accumulationUserAmount ))

    currentUserCount.update(currentUserAmount)
    accumulationUserCount.update(accumulationUserAmount)
    peekUserCount.update(peekUserAmount)
    startTimeOfRoom.update(startTime)
    endTimeOfRoom.update(endTime)
    roomStatus.update(roomState)
  }
}
