package com.flink.common.sink

import com.flink.common.domain.StatusMetric
import com.google.gson.GsonBuilder

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

class StatusOpenFalconSink extends RichSinkFunction[(String, Integer, Long, String, String,String,Integer,
    String,String,Integer,String,String,String)] {

  override def invoke(value:(String, Integer, Long, String, String,String,Integer,
    String,String,Integer,String,String,String)): Unit = {

    val userId = value._1;
    val sType = value._2  // 1: 发布流，2: 订阅流
    val time = value._3;

//    val br = value._4.toDouble;
//    val lostPre = value._5.toDouble;
//    val frt = value._6.toDouble;
//    val delay = value._7.toDouble;

    val brStr = value._4
    val lostPreStr = value._5
    val frtStr = value._6
    val delayStr = value._7.toString


    val appId:String = value._8
    val roomId:String = value._9
    val mType:Integer = value._10
    val  rpc_id:String = value._11
    val sid:String = value._12
    val streamId:String = value._13

    val tags = "userId=" + userId + "," +
                "roomId=" + roomId + "," +
                "streamId=" + streamId + "," +
                "stype=" + sType + "," +
                "mType=" +  mType + "," +
                "rpc_id=" + rpc_id + "," +
                "sid=" + sid

    val statusMetric: StatusMetric = new StatusMetric
    statusMetric.setEndpoint(appId)
    statusMetric.setMetric("biteRate")
//    bill.setTimestamp(new Date().getTime)
    statusMetric.setTimestamp(time)
    statusMetric.setStep(60)
    statusMetric.setValue(brStr.toLong)
    statusMetric.setCounterType("GAUGE")
    statusMetric.setTags(tags)

    insertFalcon(statusMetric)
  }


  val OPENFALCON_URL="http://172.16.177.172:1988/v1/push"

//  def insertFalcon(ts: util.List[StatusMetric]): Unit = {
def insertFalcon(ts: StatusMetric): Unit = {
    val restTemplate: RestTemplate = new RestTemplate
    val headers: HttpHeaders = new HttpHeaders
    val contentType: MediaType = MediaType.parseMediaType("application/json; charset=UTF-8")
    headers.setContentType(contentType)
    headers.add("Accept", MediaType.APPLICATION_JSON.toString)
    //      val ts: java.util.List[StatusMetric] = new java.util.ArrayList[StatusMetric]

    //    val gson: Gson = new Gson
    val gson = new GsonBuilder().disableHtmlEscaping().create();
    val s = gson.toJson(ts)
    print("s" + s)
    print("------")
    //      val s = JSON.toJSONString(ts)
    val formEntity: HttpEntity[String] = new HttpEntity[String](s, headers)
    //    val body: String = restTemplate.postForEntity("http://10.25.27.40:1988/v1/push", formEntity, classOf[String]).getBody
    val body: String = restTemplate.postForEntity(OPENFALCON_URL, formEntity, classOf[String]).getBody
//    logger.info("request  body is: " + s)
//    logger.info("response  body is: " + body.toString)
  }

  override def open( parameters:Configuration) {
  }

  override def close(): Unit = {
  }
}
