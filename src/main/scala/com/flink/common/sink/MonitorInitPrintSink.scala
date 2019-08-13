package com.flink.common.sink

import com.flink.common.mybatis.RoomStatusDataAccess
import org.apache.flink.streaming.api.functions.sink.SinkFunction

class MonitorInitPrintSink extends SinkFunction[(String,  String,Long, Long, Long, Boolean, Integer,Integer,Integer)] {
  override def invoke(value: (String,  String, Long, Long, Long, Boolean, Integer,Integer,Integer)): Unit = {

    RoomStatusDataAccess.insertDB( value )
    println("MonitorInitPrintSink",value)
  }
}

//class MonitorPrintSink extends SinkFunction[(String, Long, Long, String, String)] {
//
////  override def invoke(value:(String, Integer, Long, String, String,String,Integer)): Unit = {
////
////  }
//}


