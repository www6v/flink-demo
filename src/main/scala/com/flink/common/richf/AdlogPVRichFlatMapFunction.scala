package com.flink.common.richf

import com.flink.common.bean.{AdlogBean, StatisticalIndic}
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{MapStateDescriptor, MapState, ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.util.Collector

import java.util.Map.Entry;
import java.util.Iterator;

class AdlogPVRichFlatMapFunction
  extends RichFlatMapFunction[AdlogBean, (String, Long, Long, String, String)]
//    with ListCheckpointed[(String, Long, Long, String, String)]
{

//  var mapState: MapState[Long,AdlogBean] = _
  private var metric:(String, Long, Long, String, String) = _

  override def flatMap(value: AdlogBean, out: Collector[(String, Long, Long, String, String)]): Unit = {
//    mapState.put(value.time, value)
//
//    val itor: Iterator[Entry[Long, AdlogBean]] = mapState.iterator();
//
//    while(itor.hasNext) {
//      val next = itor.next()
//      val timeKey = next.getKey
//      val v = next.getValue
//
//      out.collect((value.userId, timeKey, v.time, v.br, v.lostpre ))
//    }

    metric = (value.userId, value.time, value.time, value.br, value.lostpre )
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
