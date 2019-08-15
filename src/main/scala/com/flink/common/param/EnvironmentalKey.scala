package com.flink.common.param

trait EnvironmentalKey {
  val HBASE_ZOOKEEPER = ""
//  val KAFKA_ZOOKEEPER = "ucloud-cdh-04,ucloud-cdh-05,ucloud-cdh-06"
  val KAFKA_ZOOKEEPER = "172.25.15.161:2181,172.25.10.135:2181,172.25.5.62:2181"
//  val BROKER = "kafka-1:9092,kafka-2:9092,kafka-3:9092"
val BROKER = "172.25.15.161:9092,172.25.10.135:9092,172.25.5.62:9092"
//  val TOPIC = "smartadsdeliverylog"
//  val TOPIC = "flink-kafka-1"

//  val TOPIC = "flink-rtc-log"
//  val TOPIC = "flink-rtc-log-2" // fluentd
  val TOPIC = "flink-rtc-log-3"  /// myself
}
