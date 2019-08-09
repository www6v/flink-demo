package com.flink.common.sink


import org.apache.flink.streaming.api.functions.sink.{SinkFunction, RichSinkFunction}

class MonitorPrintSink extends SinkFunction[(String, Integer, Long, String, String,String,Integer)] {
  override def invoke(value: (String, Integer, Long, String, String,String,Integer)): Unit = {
    println("MonitorPrintSink",value)
  }
}

//class MonitorPrintSink extends SinkFunction[(String, Long, Long, String, String)] {
//
////  override def invoke(value:(String, Integer, Long, String, String,String,Integer)): Unit = {
////
////  }
//}


