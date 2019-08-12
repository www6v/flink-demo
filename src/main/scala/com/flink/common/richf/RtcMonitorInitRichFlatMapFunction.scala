package com.flink.common.richf

import com.flink.common.bean.{MonitorRoomBean, MonitorBean}
import com.flink.common.entry.Constants
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{ValueStateDescriptor, ValueState}
import org.apache.flink.configuration.Configuration
import org.apache.flink.util.Collector;

class RtcMonitorInitRichFlatMapFunction
  extends RichFlatMapFunction[MonitorRoomBean, (String,  String, Long, Integer,Integer)]
{
  private var currentUserCount: ValueState[Integer] = _
  private var accumulationUserCount: ValueState[Integer] = _

//  private var metric:(String,  String, Long, Integer) = _

  override def open(parameters: Configuration): Unit = {
    val userCountDescriptor = new ValueStateDescriptor[Integer]("currentUserCount", classOf[Integer], 0)  /// 要设置默认值0
    currentUserCount = getRuntimeContext.getState[Integer](userCountDescriptor)

    val accumulationUserCountDescriptor = new ValueStateDescriptor[Integer]("accumulationUserCount", classOf[Integer], 0)  /// 要设置默认值0
    accumulationUserCount = getRuntimeContext.getState[Integer](accumulationUserCountDescriptor)
  }

  override def flatMap(value: MonitorRoomBean, out: Collector[(String,  String, Long, Integer,Integer)]): Unit = {
    var currentUserAmount = currentUserCount.value
    var accumulationUserAmount = accumulationUserCount.value()

    val roomId: String = value.roomId
    val userId: String = value.userId
    val time: Long = value.time

//    metric = (roomId, userId, time, currentUserAmount)
    out.collect((roomId, userId, time, currentUserAmount,accumulationUserAmount ))

    if (value.statusType == Constants.STATUS_TYPE_INIT ) {
      println("roomId", roomId , "userId", userId, "join")
      currentUserAmount += 1
    }
    if ( value.statusType == Constants.STATUS_TYPE_LEAVE ) {
      println("roomId",roomId, "userId", userId, "leave")
      currentUserAmount -= 1
    }
    accumulationUserAmount += 1

    currentUserCount.update(currentUserAmount)
    accumulationUserCount.update(accumulationUserAmount)
  }
}
