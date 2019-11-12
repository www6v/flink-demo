package com.flink.common.sink

import com.flink.common.mybatis.repository.OperationDataAccess
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction

class OperationSink extends RichSinkFunction[(String, String, String, Integer, Long, Integer)] {

  override def invoke(value:(String, String, String, Integer, Long, Integer)): Unit = {
    OperationDataAccess.insertDB(value)
  }

  override def open( parameters:Configuration) {
  }

  override def close(): Unit = {
  }
}
