package com.flink.common.richf

import com.flink.common.bean.{MonitorRoomBean, MonitorBean}
import com.flink.common.entry.Constants
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{ValueStateDescriptor, ValueState}
import org.apache.flink.configuration.Configuration
import org.apache.flink.util.Collector;

class RtcMonitorInitRichFlatMapFunction
  extends RichFlatMapFunction[MonitorRoomBean, (String,  String, Long, Integer)]
{
  private var userCount: ValueState[Integer] = _

//  private var metric:(String,  String, Long, Integer) = _

  override def open(parameters: Configuration): Unit = {
    val userCountDescriptor = new ValueStateDescriptor[Integer]("userCount", classOf[Integer], 0)  /// 要设置默认值0
    userCount = getRuntimeContext.getState[Integer](userCountDescriptor)
  }

  override def flatMap(value: MonitorRoomBean, out: Collector[(String,  String, Long, Integer)]): Unit = {
    var userAmount = userCount.value

    val roomId: String = value.roomId
    val userId: String = value.userId
    val time: Long = value.time

//    println("roomId",roomId)
//    println("userId",userId)
//    println("userAmount",userAmount)

//    metric = (roomId, userId, time, userAmount)
    out.collect((roomId, userId, time, userAmount))

    if ( value.statusType == Constants.STATUS_TYPE_INIT ) {
      println("roomId-",roomId, "-join")
      userAmount += 1
    }
    if ( value.statusType == Constants.STATUS_TYPE_LEAVE ) {
      println("roomId-",roomId, "-leave")
      userAmount -= 1
    }
    this.userCount.update(userAmount)
  }

//  override def open(parameters: Configuration): Unit = {
//    val mapDesc = new MapStateDescriptor[Long,AdlogBean]("StatisticalIndic", classOf[(Long)], classOf[(AdlogBean)] ) /// StatisticalIndic(0)
//    mapState = getRuntimeContext().getMapState(mapDesc)
//  }

//  override def restoreState(list: util.List[(String, Long, Long, String, String)]): Unit = {
//  }
//
//  override def snapshotState(l: Long, l1: Long): util.List[(String, Long, Long, String, String)] = {
//          val list : util.List[(String, Long, Long, String, String)] = new util.ArrayList();
//          list.add(metric)
//          list
//  }
}
