package com.lozuwa.kafkametricsexporter.kafka.service;

import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaTopic;
import org.apache.kafka.clients.admin.AdminClient;

import java.util.List;

public interface KafkaTopicService {

  public List<KafkaTopic> describeTopics(AdminClient admin);

}
