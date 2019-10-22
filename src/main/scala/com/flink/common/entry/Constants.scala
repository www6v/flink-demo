package com.flink.common.entry

/**
  * Created by www6v on 2019/8/12.
  */
object Constants {
  val STATUS_TYPE_INIT = 1
  val STATUS_TYPE_STATUS = 2
  val STATUS_TYPE_LEAVE = 3
  val STATUS_TYPE_EXCEPTION = 4
  val STATUS_TYPE_OPERATION = 5

  val STREAM_PUB: Int = 1
  val STREAM_SUB: Int = 2

  val BITE_RATE_AUDIO: String = "biteRateAudio"
  val LOST_PRE_AUDIO: String = "lostPreAudio"
  val VOLUME_AUDIO: String = "volumeAudio"

    val OPENFALCON_URL = "http://172.16.177.172:1988/v1/push"  /// pre
//  val OPENFALCON_URL = "http://172.16.236.74:1988/v1/push"   /// prd

}
