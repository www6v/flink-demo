package com.flink.common

import com.flink.common.domain.common.Data
import com.flink.common.domain.exception.ExceptionData
import com.flink.common.domain.joinLeave.InitData
import com.flink.common.domain.operation.OpertionData

import scala.beans.BeanProperty

package object bean {
  @Deprecated
  case class AdlogBean(
                        val userId: String,
                        val sType: Integer,
                        val br: String,
                        val lostPre: String,
                        val frt : String,
                        val delay: Integer,
                        val time:Long,
                        var pv: StatisticalIndic) {
    //    val key = s"${userId} - ${br} - ${time} - ${lostpre}"
    @BeanProperty
    val key = s"${userId}"

    override def toString() = {
      (key, pv).toString()
    }
  }

  case class StatisticalIndic(var pv: Int){
    override def toString()={
      pv.toString
    }
  }


  case class MonitorStatusBean(
                        val roomId: String,
                        val userId: String,
                        val sType: Integer,
                        val br: String,
                        val lostPre: String,
                        val frt : String,
                        val delay: Integer,
                        val time:Long,

                        val aid:String,
                        val mType:Integer,
                        val  rpc_id:String,
                        val sid:String,
                        val streamid:String
//                        var pv: StatisticalIndic
                              ) {
    //    val key = s"${userId} - ${br} - ${time} - ${lostpre}"
    //    val key = s"${userId}"
    @BeanProperty
    val key = s"${roomId}"

    override def toString() = {
      (key).toString()
    }
    //      (key, pv).toString()
  }


  case class MonitorRoomBean(
                          val roomId: String,
                          val userId: String,
                          val statusType:Integer,
                          val time:Long,
                          val data:InitData) {
    //    val key = s"${userId} - ${br} - ${time} - ${lostpre}"
    @BeanProperty
    val key = s"${roomId}"
    //    val key = s"${userId}"

    override def toString() = {
      (key).toString()
    }
  }

  case class MonitorOpertionBean(
                              val roomId: String,
                              val userId: String,
                              val statusType:Integer,
                              val time:Long,
                              val data:OpertionData) {
    @BeanProperty
    val key = s"${roomId}"
    //    val key = s"${userId}"

    override def toString() = {
      (key).toString()
    }
  }

  case class MonitorExceptionBean(
                                  val roomId: String,
                                  val userId: String,
                                  val statusType:Integer,
                                  val time:Long,
                                  val data:ExceptionData) {
    @BeanProperty
    val key = s"${roomId}"
    //    val key = s"${userId}"

    override def toString() = {
      (key).toString()
    }
  }
}
