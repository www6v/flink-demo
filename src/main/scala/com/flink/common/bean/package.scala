package com.flink.common

import scala.beans.BeanProperty
import com.flink.common.domain.{InitData, Data}

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
                        var pv: StatisticalIndic) {
    //    val key = s"${userId} - ${br} - ${time} - ${lostpre}"
    @BeanProperty
    val key = s"${roomId}"
    //    val key = s"${userId}"

    override def toString() = {
      (key, pv).toString()
    }
  }


  case class MonitorRoomBean(
                          val roomId: String,
                          val userId: String,
                          val statusType:Integer,
                          val time:Long,
                          val data: Data) {
    //    val key = s"${userId} - ${br} - ${time} - ${lostpre}"
    @BeanProperty
    val key = s"${roomId}"
    //    val key = s"${userId}"

    override def toString() = {
      (key).toString()
    }
  }
}
