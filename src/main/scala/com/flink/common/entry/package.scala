package com.flink.common

import java.util.Properties

import com.flink.common.param.{PrdEnvironmentalKey, PreEnvironmentalKey}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup

package object entry extends PreEnvironmentalKey {
//package object entry extends PrdEnvironmentalKey {

  def getFlinkEnv(checkpointPath: String,interval:Long = 6000) = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

//    env.enableCheckpointing(interval) //更新offsets。每60s提交一次
//    //超时
//    //env.getCheckpointConfig.setCheckpointTimeout(5000)
//    // 同一时间只允许进行一个检查点
//    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1);
//    // 表示一旦Flink处理程序被cancel后，会保留Checkpoint数据，以便根据实际需要恢复到指定的Checkpoint
//    env.getCheckpointConfig.enableExternalizedCheckpoints(
//      ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
//    env.setStateBackend(new FsStateBackend(checkpointPath))
////    val rocksDBStateBackend = new RocksDBStateBackend(checkpointPath)
////    rocksDBStateBackend.setDbStoragePath(checkpointPath + "/rocksdbstorage")
////    env.setStateBackend(rocksDBStateBackend)

    env
  }

  /**
    *
    */
  def getKafkaParam(broker: String) = {
    val pro = new Properties()
    pro.put("bootstrap.servers", broker)
    pro.put("zookeeper.connect", KAFKA_ZOOKEEPER)
    pro.put("group.id", "test")
    pro.put("auto.commit.enable", "true") //kafka 0.8-
    pro.put("enable.auto.commit", "true") //kafka 0.9+
    pro.put("auto.commit.interval.ms", "60000")
    pro
  }

}
