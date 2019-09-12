package com.flink.common.param

trait EnvironmentalKey {
  val HBASE_ZOOKEEPER = ""
//  val KAFKA_ZOOKEEPER = "172.25.15.161:2181,172.25.10.135:2181,172.25.5.62:2181"
//  val KAFKA_ZOOKEEPER = "172.16.125.89:2181,172.16.245.89:2181,172.16.217.215:2181"  // new  pre
  val KAFKA_ZOOKEEPER = "172.16.220.64:2181,172.16.44.47:2181,172.16.68.114:2181"  // new  prd


// val BROKER = "172.25.15.161:9092,172.25.10.135:9092,172.25.5.62:9092"
//  val BROKER = "172.16.125.89:9092,172.16.245.89:9092,172.16.217.215:9092"  /// new  pre
  val BROKER = "172.16.220.64:9092,172.16.44.47:9092,172.16.68.114:9092"  /// new  prd


//  val TOPIC = "flink-kafka-1"
//  val TOPIC = "flink-rtc-log"
  val TOPIC_INIT_LEAVE = "flink-rtc-log-2" // fluentd
  val TOPIC_Process = "flink-rtc-log-3"  /// fluentd
}
