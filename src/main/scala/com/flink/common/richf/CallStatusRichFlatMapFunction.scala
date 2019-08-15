package com.flink.common.richf

import com.flink.common.bean.{MonitorStatusBean, AdlogBean}
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.util.Collector;

class CallStatusRichFlatMapFunction
  extends RichFlatMapFunction[MonitorStatusBean, (String, Integer, Long, String, String,String,Integer)]
//    with ListCheckpointed[(String, Long, Long, String, String)]
{
  private var metric:(String, Integer, Long, String, String,String,Integer) = _

  override def flatMap(value: MonitorStatusBean, out: Collector[(String, Integer, Long, String, String,String,Integer)]): Unit = {
    val userId: String = value.userId
    val sType: Integer = value.sType
    val time: Long = value.time
    val br: String = value.br
    val lostPre: String = value.lostPre
    val frt : String = value.frt
    val delay: Integer = value.delay

    metric = (userId, sType, time, br, lostPre, frt, delay)
    out.collect(metric)
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
