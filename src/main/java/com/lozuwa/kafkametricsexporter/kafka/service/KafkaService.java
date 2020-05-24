package com.lozuwa.kafkametricsexporter.kafka.service;

import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaConsumerGroup;
import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaTopic;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.common.PartitionInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface KafkaService {

  public List<KafkaTopic> loadKafkaTopicsToCollection();

  public List<KafkaConsumerGroup> loadKafkaConsumerGroupsToCollection();

}
