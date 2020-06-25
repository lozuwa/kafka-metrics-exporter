package com.lozuwa.kafkametricsexporter.kafka.Model;

public class KafkaTopicPartition {

  private int partitionId;
  private int leaderId;
  private int replicationFactor;
  private int inSyncReplicas;
  private int offlineReplicas;
  private int currentOffset;
  private int endOffset;

  public KafkaTopicPartition(){
  }

  public int getPartitionId() {
    return partitionId;
  }

  public void setPartitionId(int partitionId) {
    this.partitionId = partitionId;
  }

  public int getLeaderId() {
    return leaderId;
  }

  public void setLeaderId(int leaderId) {
    this.leaderId = leaderId;
  }

  public int getReplicationFactor() {
    return replicationFactor;
  }

  public void setReplicationFactor(int replicationFactor) {
    this.replicationFactor = replicationFactor;
  }

  public int getInSyncReplicas() {
    return inSyncReplicas;
  }

  public void setInSyncReplicas(int inSyncReplicas) {
    this.inSyncReplicas = inSyncReplicas;
  }

  public int getOfflineReplicas() {
    return offlineReplicas;
  }

  public void setOfflineReplicas(int offlineReplicas) {
    this.offlineReplicas = offlineReplicas;
  }
}
