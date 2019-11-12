package com.flink.common.richf

import com.flink.common.bean.{MonitorOpertionBean}
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.util.Collector;

class OperationRichFlatMapFunction
  extends RichFlatMapFunction[MonitorOpertionBean, (String, String, String, Integer, Long, Integer)]
//    with ListCheckpointed[(String, Long, Long, String, String)]
{
  private var metric:(String, String, String, Integer, Long, Integer) = _

  override def flatMap(value: MonitorOpertionBean, out: Collector[(String, String, String, Integer, Long, Integer)]): Unit = {
    val appId: String = value.appId;

    val roomId: String = value.roomId;
    val userId: String = value.userId
    val statusType: Integer =value.statusType
    val time: Long = value.time
    val opertionType: Integer = value.data.getOpertionType;

    metric = (appId, roomId, userId, statusType, time, opertionType)

    out.collect(metric)
  }
}


//        val sType: Integer = value.sType
//        val br: String = value.br
//        val lostPre: String = value.lostPre
//        val frt : String = value.frt
//        val delay: Integer = value.delay