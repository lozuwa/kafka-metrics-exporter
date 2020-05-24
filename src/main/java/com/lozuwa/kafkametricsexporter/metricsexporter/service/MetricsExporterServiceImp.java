package com.lozuwa.kafkametricsexporter.metricsexporter.service;

import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaConsumerGroup;
import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaTopic;
import com.lozuwa.kafkametricsexporter.kafka.service.KafkaService;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
@Configuration
public class MetricsExporterServiceImp implements MetricsExporterService {

  private final static Logger logger = Logger.getLogger(MetricsExporterServiceImp.class.getName());

  @Autowired
  private KafkaService kafkaService;

  @Autowired
  private PrometheusMetricsConverterService prometheusMetricsConverterService;

  public MetricsExporterServiceImp(KafkaService kafkaService, PrometheusMetricsConverterService prometheusMetricsConverterService){
    this.kafkaService = kafkaService;
    this.prometheusMetricsConverterService = prometheusMetricsConverterService;
  }

  @Override
  public String getKafkaTopicMetrics(){
    logger.info("Getting list of topics from kafka.");
    List<KafkaTopic> kafkaTopics = getTopics();
    logger.info("Transforming into plaintext metrics.");
    String response = prometheusMetricsConverterService.kafkaTopicsFromJsonToPlaintext(kafkaTopics);
    return response;
  }

  @Override
  public List<KafkaTopic> getMetricsFromCompactedTopics(){
    logger.info("Getting list of topics from kafka.");
    List<KafkaTopic> kafkaTopics = getTopics();
    logger.info("Filtering compacted topics.");
    kafkaTopics = filterCompactedTopics(kafkaTopics);
    return kafkaTopics;
  }

  @Override
  public List<KafkaConsumerGroup> getMetricsFromConsumerGroupIds(){
    logger.info("Getting list of ConsumerGroups.");
    List<KafkaConsumerGroup> kafkaConsumerGroups = describekafkaConsumerGroups();
    return kafkaConsumerGroups;
  }

  public List<KafkaTopic> getTopics(){
    // Get list of topics.
    AdminClient admin = kafkaService.getAdminClient();
    List<KafkaTopic> kafkaTopics = kafkaService.describeTopics(admin);
    kafkaService.closeAdminClient(admin);
    return kafkaTopics;
  }

  public List<KafkaTopic> filterCompactedTopics(List<KafkaTopic> kafkaTopics){
    // Local variables.
    List<KafkaTopic> compactedKafkaTopics = new ArrayList<>();
    // Filter out compacted topics.
    for (KafkaTopic topic: kafkaTopics){
      boolean compactedTopic = false;
      HashMap<String, String> topicConfigurations = topic.getTopicConfigurations();
      for (Map.Entry<String, String> conf: topicConfigurations.entrySet()){
        logger.finest("Conf: " + conf.getKey() + " = " + conf.getValue());
      }
      Boolean hasCleanupPolicy = topicConfigurations.containsKey("cleanup.policy");
      if (hasCleanupPolicy){
        String cleanupPolicy = topicConfigurations.get("cleanup.policy");
        if (cleanupPolicy.equals("compact")){
          compactedTopic = true;
        }
      }
      if (compactedTopic){
        compactedKafkaTopics.add(topic);
      }
    }
    return compactedKafkaTopics;
  }

  public List<KafkaConsumerGroup> describekafkaConsumerGroups(){
    AdminClient admin = kafkaService.getAdminClient();
    List<KafkaConsumerGroup> kafkaConsumerGroups = kafkaService.describeConsumerGroups(admin);
    kafkaService.closeAdminClient(admin);
    return kafkaConsumerGroups;
  }

}
