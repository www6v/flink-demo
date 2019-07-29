package com.flink.common

import scala.beans.BeanProperty

package object bean {

  case class AdlogBean(
                        var userId: String,
                        var br: String,
                        var lostpre: String,
                        var time:Long,
                        var pv: StatisticalIndic) {
    @BeanProperty
    val key = s"${userId} - ${br} - ${time} - ${lostpre}"

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
