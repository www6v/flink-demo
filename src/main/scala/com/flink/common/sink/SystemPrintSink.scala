package com.flink.common.sink

import com.flink.common.bean.{AdlogBean, StatisticalIndic}
import io.prometheus.client.Gauge
import io.prometheus.client.exporter.PushGateway
import org.apache.flink.streaming.api.functions.sink.{
  RichSinkFunction,
  SinkFunction
}
import org.apache.flink.configuration.Configuration

//class SystemPrintSink extends SinkFunction[AdlogBean] {
  class SystemPrintSink extends RichSinkFunction[(String, Integer, Long, String, String,String,Integer)] {
//class SystemPrintSink extends SinkFunction[(String, Long, Long, String, String)] {


  var prometheusPush: PushGateway = _
  var gaugeBr: Gauge = _
  var gaugeLostPre: Gauge = _
  var gaugeFrt: Gauge = _
  var gaugeDelay: Gauge = _


  override def invoke(value:(String, Integer, Long, String, String,String,Integer)): Unit = {
//  override def invoke(value: AdlogBean): Unit = {
//    println("SystemPrintSink",value)

    val userId = value._1;
    val stype = value._2
    val time = value._3;
    val br = value._4.toDouble;
    val lostPre = value._5.toDouble;
    val frt = value._6.toDouble;
    val delay = value._7.toDouble;

    gaugeBr.labels(userId).set(br)
    prometheusPush.push(gaugeBr, "biteRateOfUser")

    gaugeLostPre.labels(userId).set(lostPre)
    prometheusPush.push(gaugeLostPre, "lostPreOfUser")

    gaugeFrt.labels(userId).set(frt)
    prometheusPush.push(gaugeFrt, "frtOfUser")

    gaugeDelay.labels(userId).set(delay)
    prometheusPush.push(gaugeDelay, "delayOfUser")
  }

  override def open( parameters:Configuration) {

     prometheusPush = new PushGateway("prometheus-gateway.app.pre.urome.cn")

     gaugeBr  = Gauge.build.name("biteRateOfTheUser").
      labelNames("userid" ).
      help("rtc monitor").register

    gaugeLostPre = Gauge.build.name("lostPreOfTheUser").
      labelNames("userid" ).
      help("rtc monitor").register

    gaugeFrt  = Gauge.build.name("frtOfTheUser").
      labelNames("userid" ).
      help("rtc monitor").register

    gaugeDelay  = Gauge.build.name("delayOfTheUser").
      labelNames("userid" ).
      help("rtc monitor").register
  }

  override def close(): Unit = {
    gaugeBr.clear()
    gaugeLostPre.clear()
    gaugeFrt.clear()
    gaugeDelay.clear()
  }
}
