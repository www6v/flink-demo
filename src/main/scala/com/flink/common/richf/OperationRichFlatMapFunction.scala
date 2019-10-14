package com.flink.common.richf

import com.flink.common.bean.{MonitorOpertionBean, MonitorStatusBean}
import com.flink.common.domain.operation.OpertionData
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.util.Collector;

class OperationRichFlatMapFunction
  extends RichFlatMapFunction[MonitorOpertionBean, (String, String, Integer, Long, Integer)]
//    with ListCheckpointed[(String, Long, Long, String, String)]
{
  private var metric:(String, String, Integer, Long, Integer) = _

  override def flatMap(value: MonitorOpertionBean, out: Collector[(String, String, Integer, Long, Integer)]): Unit = {
    val roomId: String = value.roomId;
    val userId: String = value.userId
    val statusType: Integer =value.statusType
    val time: Long = value.time
    val opertionType: Integer = value.data.getOpertionType;

    metric = (roomId, userId, statusType, time, opertionType)

    out.collect(metric)
  }
}


//        val sType: Integer = value.sType
//        val br: String = value.br
//        val lostPre: String = value.lostPre
//        val frt : String = value.frt
//        val delay: Integer = value.delay