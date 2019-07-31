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
  class SystemPrintSink extends RichSinkFunction[(String, Long, Long, String, String)] {
//class SystemPrintSink extends SinkFunction[(String, Long, Long, String, String)] {


  var prometheusPush: PushGateway = _
  var gauge: Gauge = _
  var gauge1: Gauge = _

  override def invoke(value:(String, Long, Long, String, String) ): Unit = {
//  override def invoke(value: AdlogBean): Unit = {
//    println("SystemPrintSink",value)


    val userid = value._1;
    val time = value._2;
    val br = value._4.toDouble;

    val lostRate =value._5.toDouble;

    gauge.labels(userid).set(br)
    prometheusPush.push(gauge, "biteRateOfUser")

    gauge1.labels(userid).set(lostRate)
    prometheusPush.push(gauge1, "lostRateOfUser")
  }

  override def open( parameters:Configuration) {

     prometheusPush = new PushGateway("prometheus-gateway.app.pre.urome.cn")
     gauge  = Gauge.build.name("biteRateOfTheUser").
      labelNames("userid" ).
      help("rtc monitor").register

    gauge1 = Gauge.build.name("lostRateOfTheUser").
      labelNames("userid" ).
      help("rtc monitor").register
  }

  override def close(): Unit = {
    gauge.clear()
    gauge1.clear()
  }
}
