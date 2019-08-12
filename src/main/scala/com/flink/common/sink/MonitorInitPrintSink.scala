package com.flink.common.sink

import org.apache.flink.streaming.api.functions.sink.SinkFunction

class MonitorInitPrintSink extends SinkFunction[(String,  String, Long, Integer)] {
  override def invoke(value: (String,  String, Long, Integer)): Unit = {
    println("MonitorInitPrintSink",value)
  }
}

//class MonitorPrintSink extends SinkFunction[(String, Long, Long, String, String)] {
//
////  override def invoke(value:(String, Integer, Long, String, String,String,Integer)): Unit = {
////
////  }
//}


