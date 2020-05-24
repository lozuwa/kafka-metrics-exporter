package com.lozuwa.kafkametricsexporter.metricsexporter.service;

import com.lozuwa.kafkametricsexporter.Utils.Utils;
import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaTopic;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrometheusMetricsConverterServiceImp implements PrometheusMetricsConverterService {

  public static final String job = "kakfa";

  @Override
  public String kafkaTopicsFromJsonToPlaintext(List<KafkaTopic> kafkaTopics){
    // Local variables.
    String line = "";
    StringBuilder builder = new StringBuilder();
    String partitionsMetricLine = "kafka_topic_partitions'{'job=\"{0}\",topic=\"{1}\",{2}'}' {3}\n";
    //String partitionsMetricLine = "kafka_topic_partitions{job:\"{0}\",topic:\"{1}\",{2}} {3}\n";
    String replicationFactorMetricLine = "kafka_topic_replication_factor'{'job=\"{0}\",topic=\"{1}\",{2}'}' {3}\n";
    // Create plaintext response.
    for (KafkaTopic kafkaTopic: kafkaTopics){
      // Get class fields.
      String topicName = kafkaTopic.getTopicName();
      String partitions = String.valueOf(kafkaTopic.getPartitions());
      String replicationFactor = String.valueOf(kafkaTopic.getReplicationFactor());
      HashMap<String, String> topicConfigurations = kafkaTopic.getTopicConfigurations();
      // Configurations.
      StringBuilder configBuilder = new StringBuilder();
      int size = topicConfigurations.size();
      int counter = 0;
      for(Map.Entry<String, String> configuration: topicConfigurations.entrySet()){
        String configurationKey = configuration.getKey();
        String configurationValue = configuration.getValue();
        if (counter+1 == size){
          line = Utils.StringFormatter("{0}=\"{1}\"", configurationKey, configurationValue);
        } else{
          line = Utils.StringFormatter("{0}=\"{1}\",", configurationKey, configurationValue);
          counter++;
        }
        configBuilder.append(line);
      }
      String configsLine = configBuilder.toString();
      // Write metrics.
      // Partitions metric.
      line = Utils.StringFormatter(partitionsMetricLine, job, topicName, configsLine, partitions);
      builder.append(line);
      // Replication factor metric.
      line = Utils.StringFormatter(replicationFactorMetricLine, job, topicName, configsLine, partitions);
      builder.append(line);
    }
    // Return response.
    return builder.toString();
  }

}
