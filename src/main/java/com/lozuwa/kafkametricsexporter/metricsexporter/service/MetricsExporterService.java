package com.lozuwa.kafkametricsexporter.metricsexporter.service;

import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaConsumerGroup;
import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaTopic;

import java.util.List;

public interface MetricsExporterService {

  public String getKafkaTopicMetrics();

  public List<KafkaTopic> getMetricsFromCompactedTopics();

  public List<KafkaConsumerGroup> getMetricsFromConsumerGroupIds();

}
