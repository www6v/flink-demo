# 有修改 可以运行

# flink-demo 
flink 1.8.0 <br>
kafka 0.10 <br>
整体流程示意 <br>

kafka ->  flink (state管理) -> hbase(redis)

由于flink 带有的checkpoint ，所以可以实现 准确的一次性统计
