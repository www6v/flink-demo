package com.flink.common.richf

import com.flink.common.bean.{MonitorAudioStatusBean, MonitorStatusBean}
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.util.Collector;

class StatusAudioRichFlatMapFunction
  extends RichFlatMapFunction[MonitorAudioStatusBean, (String, Integer, Long,
    String, String,String,
    String,String,Integer,String,String,String)]
{
  private var metric:(String, Integer, Long,
    String, String,String,
    String,String,Integer,String,String,String) = _

  override def flatMap(value: MonitorAudioStatusBean, out: Collector[(String, Integer, Long,
    String, String,String,
    String,String,Integer,String,String,String)]): Unit = {
    val userId: String = value.userId
    val sType: Integer = value.sType
    val time: Long = value.time
    val br: String = value.br
    val lostPre: String = value.lostPre
    val volume: String = value.volume
//    val frt : String = value.frt
//    val delay: Integer = value.delay

    val aid:String = value.aid
    val roomId:String = value.roomId
    val mType:Integer = value.mType
    val rpc_id:String = value.rpc_id
    val sid:String = value.sid
    val streamId:String = value.streamid

    metric = (userId, sType, time,
              br, lostPre, volume,
              aid, roomId, mType, rpc_id, sid, streamId)

    out.collect(metric)
  }





//  override def open(parameters: Configuration): Unit = {
//    val mapDesc = new MapStateDescriptor[Long,AdlogBean]("StatisticalIndic", classOf[(Long)], classOf[(AdlogBean)] ) /// StatisticalIndic(0)
//    mapState = getRuntimeContext().getMapState(mapDesc)
//  }

//  override def restoreState(list: util.List[(String, Long, Long, String, String)]): Unit = {
//  }
//
//  override def snapshotState(l: Long, l1: Long): util.List[(String, Long, Long, String, String)] = {
//          val list : util.List[(String, Long, Long, String, String)] = new util.ArrayList();
//          list.add(metric)
//          list
//  }
}
