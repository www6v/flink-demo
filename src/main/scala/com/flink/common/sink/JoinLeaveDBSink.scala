package com.flink.common.sink

import com.flink.common.mybatis.repository.RoomStatusDataAccess
import org.apache.flink.streaming.api.functions.sink.SinkFunction

class JoinLeaveDBSink extends SinkFunction[((String,  String,Long, Long, Long, Boolean, Integer,Integer,Integer),
  (String,String, Integer, Long, String, String, String, String, String, String,Integer, String))] {
  override def invoke(value: ((String,  String, Long, Long, Long, Boolean, Integer,Integer,Integer),
    (String, String, Integer, Long, String, String, String, String, String, String,Integer, String))): Unit = {

    RoomStatusDataAccess.insertDB(value._1)
    RoomStatusDataAccess.insertUserInfoDB(value._2)
    RoomStatusDataAccess.insertUserDetailDB(value._2)

    println("MonitorInitPrintSink",value)
  }
}

//class MonitorPrintSink extends SinkFunction[(String, Long, Long, String, String)] {
//
////  override def invoke(value:(String, Integer, Long, String, String,String,Integer)): Unit = {
////
////  }
//}


