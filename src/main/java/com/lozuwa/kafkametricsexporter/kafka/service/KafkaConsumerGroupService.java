package com.lozuwa.kafkametricsexporter.kafka.service;

import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaConsumerGroup;
import org.apache.kafka.clients.admin.AdminClient;

import java.util.List;

public interface KafkaConsumerGroupService {

  public List<KafkaConsumerGroup> describeConsumerGroups(AdminClient admin);

}
