package com.flink.common

import scala.beans.BeanProperty

package object bean {

  case class AdlogBean(
                        val userId: String,
                        val stype: Integer,
                        val br: String,
                        val lostpre: String,
                        val frt : String,
                        val delay: Integer,
                        val time:Long,
                        val pv: StatisticalIndic) {
    @BeanProperty
//    val key = s"${userId} - ${br} - ${time} - ${lostpre}"
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

}
