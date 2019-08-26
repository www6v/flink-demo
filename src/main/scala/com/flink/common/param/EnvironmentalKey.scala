package com.flink.common.param

trait EnvironmentalKey {
  val HBASE_ZOOKEEPER = ""
//  val KAFKA_ZOOKEEPER = "172.25.15.161:2181,172.25.10.135:2181,172.25.5.62:2181"
  val KAFKA_ZOOKEEPER = "172.16.125.89:2181,172.16.245.89:2181,172.16.217.215:2181"  // new

// val BROKER = "172.25.15.161:9092,172.25.10.135:9092,172.25.5.62:9092"
  val BROKER = "172.16.125.89:9092,172.16.245.89:9092,172.16.217.215:9092"  /// new

//  val TOPIC = "flink-kafka-1"
//  val TOPIC = "flink-rtc-log"
  val TOPIC = "flink-rtc-log-2" // fluentd
//  val TOPIC = "flink-rtc-log-3"  /// myself
}
