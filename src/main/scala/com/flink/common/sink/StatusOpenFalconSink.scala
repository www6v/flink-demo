package com.flink.common.sink

import com.flink.common.domain.StatusMetric
import com.google.gson.GsonBuilder

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

import java.util

class StatusOpenFalconSink extends RichSinkFunction[(String, Integer, Long, String, String,String,Integer,
    String,String,Integer,String,String,String)] {

  val BITE_RATE: String = "biteRate"
  val LOST_PRE: String = "lostPre"
  val FRT: String = "frt"
  val DELAY: String = "delay"

  override def invoke(value:(String, Integer, Long, String, String,String,Integer,
    String,String,Integer,String,String,String)): Unit = {

    val tsBiteRate: util.List[StatusMetric] = biteRateMetric(value)
    val tsLostPre: util.List[StatusMetric] = lostPreMetric(value)

    insertFalcon(tsBiteRate)
    insertFalcon(tsLostPre)
  }

  def biteRateMetric(value: (String, Integer, Long, String, String, String, Integer, String, String, Integer, String, String, String)): util.List[StatusMetric] = {
    val (userId: String, sType: Integer, time: Long,
    appId: String, roomId: String, mType: Integer, rpc_id: String, streamId: String,
    brStr: String, lostPreStr: String,frtStr: String, delayStr: String) = retriveParam(value)

    val tags: String = getTags(userId, sType, roomId, mType, rpc_id, streamId)

    val ts: util.List[StatusMetric] = fillMetric(BITE_RATE, brStr, time,  appId, tags)

    ts
  }

  def lostPreMetric(value: (String, Integer, Long, String, String, String, Integer, String, String, Integer, String, String, String)): util.List[StatusMetric] = {
    val (userId: String, sType: Integer, time: Long,
    appId: String, roomId: String, mType: Integer, rpc_id: String, streamId: String,
    brStr: String, lostPreStr: String,frtStr: String, delayStr: String) = retriveParam(value);

    val tags: String = getTags(userId, sType, roomId, mType, rpc_id, streamId)

    val ts: util.List[StatusMetric] = fillMetric(LOST_PRE, lostPreStr, time,  appId, tags)

    ts
  }

  def frtMetric(value: (String, Integer, Long, String, String, String, Integer, String, String, Integer, String, String, String)): util.List[StatusMetric] = {
    val (userId: String, sType: Integer, time: Long,
    appId: String, roomId: String, mType: Integer, rpc_id: String, streamId: String,
    brStr: String, lostPreStr: String,frtStr: String, delayStr: String) = retriveParam(value);

    val tags: String = getTags(userId, sType, roomId, mType, rpc_id, streamId)

    val ts: util.List[StatusMetric] = fillMetric(FRT, frtStr, time,  appId, tags)

    ts
  }


  def delayMetric(value: (String, Integer, Long, String, String, String, Integer, String, String, Integer, String, String, String)): util.List[StatusMetric] = {
    val (userId: String, sType: Integer, time: Long,
    appId: String, roomId: String, mType: Integer, rpc_id: String, streamId: String,
    brStr: String, lostPreStr: String,frtStr: String, delayStr: String) = retriveParam(value)

    val tags: String = getTags(userId, sType, roomId, mType, rpc_id, streamId)

    val ts: util.List[StatusMetric] = fillMetric(DELAY, delayStr, time,  appId, tags)

    ts
  }


  def retriveParam(value: (String, Integer, Long, String, String, String, Integer, String, String, Integer, String, String, String)):
  (String, Integer, Long,  String, String, Integer, String, String,
    String, String,String,String) = {
    val userId = value._1;
    val sType = value._2 // 1: 发布流，2: 订阅流
    val time = value._3;

    val brStr = value._4
    val lostPreStr = value._5
    val frtStr = value._6
    val delayStr = value._7.toString

    val appId: String = value._8
    val roomId: String = value._9
    val mType: Integer = value._10
    val rpc_id: String = value._11
    //    val sid: String = value._12
    val streamId: String = value._13

    (userId, sType, time,  appId, roomId, mType, rpc_id, streamId,
      brStr,lostPreStr,frtStr, delayStr)
  }

  def fillMetric(metricName:String, metricValue: String, time: Long, appId: String, tags: String): util.List[StatusMetric] = {
    val statusMetric: StatusMetric = new StatusMetric
    statusMetric.setEndpoint(appId)
    statusMetric.setMetric(metricName)
    //    bill.setTimestamp(new Date().getTime)
    statusMetric.setTimestamp(time)
    statusMetric.setStep(60)
    statusMetric.setValue(metricValue.toLong)
    statusMetric.setCounterType("GAUGE")
    statusMetric.setTags(tags)

    val ts: util.List[StatusMetric] = new util.ArrayList[StatusMetric]
    ts.add(statusMetric)
    ts
  }

  def getTags(userId: String, sType: Integer, roomId: String, mType: Integer, rpc_id: String, streamId: String): String = {
    val tags = "userId=" + userId + "," +
      "roomId=" + roomId + "," +
      "streamId=" + streamId + "," +
      "stype=" + sType + "," +
      "mType=" + mType + "," +
      "rpc_id=" + rpc_id
    //    + "," +  "sid=" + sid
    tags
  }

  val OPENFALCON_URL="http://172.16.177.172:1988/v1/push"

  def insertFalcon(ts: util.List[StatusMetric]): Unit = {
    val restTemplate: RestTemplate = new RestTemplate
    val headers: HttpHeaders = new HttpHeaders
    val contentType: MediaType = MediaType.parseMediaType("application/json; charset=UTF-8")
    headers.setContentType(contentType)
    headers.add("Accept", MediaType.APPLICATION_JSON.toString)
    //      val ts: java.util.List[StatusMetric] = new java.util.ArrayList[StatusMetric]

    //    val gson: Gson = new Gson
    val gson = new GsonBuilder().disableHtmlEscaping().create();
    val s = gson.toJson(ts)

    //      val s = JSON.toJSONString(ts)
    val formEntity: HttpEntity[String] = new HttpEntity[String](s, headers)
    val body: String = restTemplate.postForEntity(OPENFALCON_URL, formEntity, classOf[String]).getBody
  }

  override def open( parameters:Configuration) {
  }

  override def close(): Unit = {
  }
}
