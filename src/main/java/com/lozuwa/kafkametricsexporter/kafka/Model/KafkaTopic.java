package com.lozuwa.kafkametricsexporter.kafka.Model;

import java.util.HashMap;

public class KafkaTopic {

  private String topicName;
  private int partitions;
  private short replicationFactor;
  private HashMap<String, String> topicConfigurations;

  public KafkaTopic(String topicName, int partitions, short replicationFactor, HashMap<String, String> topicConfigurations) {
    this.topicName = topicName;
    this.partitions = partitions;
    this.replicationFactor = replicationFactor;
    this.topicConfigurations = topicConfigurations;
  }

  public String getTopicName() {
    return topicName;
  }

  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  public int getPartitions() {
    return partitions;
  }

  public void setPartitions(int partitions) {
    this.partitions = partitions;
  }

  public short getReplicationFactor() {
    return replicationFactor;
  }

  public void setReplicationFactor(short replicationFactor) {
    this.replicationFactor = replicationFactor;
  }

  public HashMap<String, String> getTopicConfigurations() {
    return topicConfigurations;
  }

  public void setTopicConfigurations(HashMap<String, String> topicConfigurations) {
    this.topicConfigurations = topicConfigurations;
  }

  @Override
  public String toString() {
    return "KafkaTopic{" +
        "topicName='" + topicName + '\'' +
        ", partitions=" + partitions +
        ", replicationFactor=" + replicationFactor +
        ", topicConfigurations=" + topicConfigurations +
        '}';
  }
}
