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
//  class SystemPrintSink extends RichSinkFunction[(String, Long, Long, String, String)] {
class SystemPrintSink extends SinkFunction[(String, Long, Long, String, String)] {

  override def invoke(value:(String, Long, Long, String, String) ): Unit = {
//  override def invoke(value: AdlogBean): Unit = {
//    println("SystemPrintSink",value)
     val prometheusPush: PushGateway = new PushGateway("prometheus-gateway.app.pre.urome.cn")
     val gaugeDemo: Gauge = Gauge.build.name("biteRateOfTheUser").
      labelNames("userid" ).
      help("rtc monitor").register

     val gaugeDemo1: Gauge = Gauge.build.name("lostRateOfTheUser").
      labelNames("userid" ).
      help("rtc monitor").register

    val userid = value._1;
    val time = value._2;
    val br = value._4.toDouble;

    val lostRate =value._5.toDouble;

    gaugeDemo.labels(userid).set(br)
    prometheusPush.push(gaugeDemo, "biteRateOfUser")

    gaugeDemo1.labels(userid).set(lostRate)
    prometheusPush.push(gaugeDemo1, "biteRateOfUser")
  }
}
