package com.lozuwa.kafkametricsexporter.kafka.Model;

public class KafkaConsumerGroup {

  private String id;
  private String state;
  private String coordinator;
  private String partitionAssignor;

  public KafkaConsumerGroup(String id, String state, String coordinator, String partitionAssignor) {
    this.id = id;
    this.state = state;
    this.coordinator = coordinator;
    this.partitionAssignor = partitionAssignor;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCoordinator() {
    return coordinator;
  }

  public void setCoordinator(String coordinator) {
    this.coordinator = coordinator;
  }

  public String getPartitionAssignor() {
    return partitionAssignor;
  }

  public void setPartitionAssignor(String partitionAssignor) {
    this.partitionAssignor = partitionAssignor;
  }

  @Override
  public String toString() {
    return "KafkaConsumerGroup{" +
        "id='" + id + '\'' +
        ", state='" + state + '\'' +
        ", coordinator='" + coordinator + '\'' +
        ", partitionAssignor='" + partitionAssignor + '\'' +
        '}';
  }

}
