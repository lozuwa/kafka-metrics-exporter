package com.lozuwa.kafkametricsexporter.metricsexporter.service;

import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaTopic;

import java.util.List;

public interface PrometheusMetricsConverterService {

  public String kafkaTopicsFromJsonToPlaintext(List<KafkaTopic> kafkaTopics);

}
