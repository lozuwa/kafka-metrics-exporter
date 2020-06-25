package com.lozuwa.kafkametricsexporter.Utils;

public class KafkaTopicMetricNames {
  // Broker.
  public static final String KAFKA_BROKERS = "kafka.brokers";

  // Topic metrics.
  public static final String KAFKA_TOPIC_PARTITIONS = "kafka.topic.partitions";
  public static final String KAFKA_TOPIC_REPLICATION_FACTOR = "kafka.topic.replication.factor";
  public static final String KAFKA_TOPIC_PARTITION_IN_SYNC_REPLICA = "kafka.topic.partition.in.sync.replica";
  public static final String KAFKA_TOPIC_PARTITION_LEADER = "kafka.topic.partition.leader";
  public static final String KAFKA_TOPIC_PARTITION_UNDER_REPLICATED_PARTITION = "kafka.topic.partition.under.replicated.partition";
  public static final String KAFKA_TOPIC_PARTITION_CURRENT_OFFSET = "";

  // Consumer group metrics.
  public static final String KAFKA_CONSUMER_GROUP_CURRENT_OFFSET = "kafka.consumergroup.current.offset";
  public static final String KAFKA_CONSUMER_GROUP_LAG = "kafka.consumergroup.lag";

}
