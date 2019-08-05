package com.flink.common.richf

import com.flink.common.bean.{AdlogBean, StatisticalIndic}
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{MapStateDescriptor, MapState, ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.util.Collector

import java.util.Map.Entry;
import java.util.Iterator;

class AdlogPVRichFlatMapFunction
  extends RichFlatMapFunction[AdlogBean, (String, Integer, Long, String, String,String,Integer)]
//    with ListCheckpointed[(String, Long, Long, String, String)]
{

//  var mapState: MapState[Long,AdlogBean] = _
  private var metric:(String, Integer, Long, String, String,String,Integer) = _

  override def flatMap(value: AdlogBean, out: Collector[(String, Integer, Long, String, String,String,Integer)]): Unit = {
//    mapState.put(value.time, value)
//
//    val itor: Iterator[Entry[Long, AdlogBean]] = mapState.iterator();
//
//    while(itor.hasNext) {
//      val next = itor.next()
//      val timeKey = next.getKey
//      val v = next.getValue
//
//      out.collect((value.userId, timeKey, v.time, v.br, v.lostPre ))
//    }

    val userId: String = value.userId
    val sType: Integer = value.stype
    val time: Long = value.time
    val br: String = value.br
    val lostPre: String = value.lostpre
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
