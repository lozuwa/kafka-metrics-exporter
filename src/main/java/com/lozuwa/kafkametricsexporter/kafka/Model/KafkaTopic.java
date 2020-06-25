package com.lozuwa.kafkametricsexporter.kafka.Model;

import java.util.HashMap;
import java.util.List;

public class KafkaTopic {

  private String topicName;
  private List<KafkaTopicPartition> partitions;
  private HashMap<String, String> topicConfigurations;

  public KafkaTopic(String topicName, List<KafkaTopicPartition> partitions, HashMap<String, String> topicConfigurations) {
    this.topicName = topicName;
    this.partitions = partitions;
    this.topicConfigurations = topicConfigurations;
  }

  public String getTopicName() {
    return topicName;
  }

  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  public List<KafkaTopicPartition> getPartitions() {
    return partitions;
  }

  public void setPartitions(List<KafkaTopicPartition> partitions) {
    this.partitions = partitions;
  }

  public HashMap<String, String> getTopicConfigurations() {
    return topicConfigurations;
  }

  public void setTopicConfigurations(HashMap<String, String> topicConfigurations) {
    this.topicConfigurations = topicConfigurations;
  }
}
