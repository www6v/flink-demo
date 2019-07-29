package com.flink.common.richf

import com.flink.common.bean.{AdlogBean, StatisticalIndic}
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{MapStateDescriptor, MapState, ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.util.Collector

class AdlogPVRichFlatMapFunction
    extends RichFlatMapFunction[AdlogBean, (String, MapState[Long,AdlogBean])] {
  var lastState: ValueState[StatisticalIndic] = _
  var mapState: MapState[Long,AdlogBean] = _

  /**
    * @desc 每次一套
    */
//  override def flatMap(value: AdlogBean, out: Collector[AdlogBean]): Unit = {
//    val ls = lastState.value()
//    val news = StatisticalIndic(ls.pv + value.pv.pv)
//    lastState.update(news)
//    value.pv = news
//
//    out.collect(value)
//  }

//  ,
  override def flatMap(value: AdlogBean, out: Collector[(String, MapState[Long,AdlogBean])]): Unit = {
//    val ls = lastState.value()
//    val news = StatisticalIndic(ls.pv + value.pv.pv)
//    lastState.update(news)
//    value.pv = news
//
//    out.collect(value)

    /////
    mapState.put(value.time, value)
    out.collect(value.userId, mapState)  /// , mapState
  }

  /**
    * @desc 当首次打开此operator的时候调用，拿到 此key的句柄
    */
  override def open(parameters: Configuration): Unit = {
//    val desc = new ValueStateDescriptor[(StatisticalIndic)]("StatisticalIndic", classOf[(StatisticalIndic)], StatisticalIndic(0))
//    //desc.setQueryable("StatisticalIndic")
//    lastState = getRuntimeContext().getState(desc)


    val mapDesc = new MapStateDescriptor[Long,AdlogBean]("StatisticalIndic", classOf[(Long)], classOf[(AdlogBean)] ) /// StatisticalIndic(0)
    mapState = getRuntimeContext().getMapState(mapDesc)
  }
}
