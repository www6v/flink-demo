package com.flink.common

import scala.beans.BeanProperty

package object bean {

  case class AdlogBean(
                        var userId: String,
                        var br: String,
                        var lostpre: String,
                        var pv: StatisticalIndic) {
    @BeanProperty
    val key = s"${br},${userId},${lostpre}"

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
