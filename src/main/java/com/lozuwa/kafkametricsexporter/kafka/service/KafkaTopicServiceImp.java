package com.lozuwa.kafkametricsexporter.kafka.service;

import com.lozuwa.kafkametricsexporter.Utils.Utils;
import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaTopic;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
@Configuration
public class KafkaTopicServiceImp implements KafkaTopicService {

  private final static Logger logger = Logger.getLogger(KafkaTopicServiceImp.class.getName());

  @Value(value = "${kafka.bootstrapAddress}")
  private String bootstrapAddress;
  @Value(value = "${kafka.consumer.groupId}")
  private String consumerGroupId;

  /**
   * Load the kafka topics to a List<KafkaTopic>.
   */
  @Override
  public List<KafkaTopic> describeTopics(AdminClient admin){
    // Local variables.
    List<KafkaTopic> kafkaTopics = new ArrayList<>();
    // List topics with a kafka consumer.
    Properties configs = loadConsumerConfigs();
    KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(configs);
    Map<String, List<PartitionInfo>> topics = consumer.listTopics();
    consumer.close();
    // Load topics to the KafkaTopic POJO.
    for (Map.Entry<String, List<PartitionInfo>> topic : topics.entrySet()) {
      // Extract kafka topic fields.
      String topicName = topic.getKey();
      int partitions = topic.getValue().size();
      short replicationFactor = (short) topic.getValue().get(0).replicas().length;
      HashMap<String, String> topicConfigurations = getTopicConfig(admin, topicName);
      logger.finest(Utils.StringFormatter("Topic: {0} partitions: {1} replication-factor: {2}", topicName, partitions, replicationFactor));
      // Load POJO.
      KafkaTopic kafkaTopic = new KafkaTopic(topicName, partitions, replicationFactor, topicConfigurations);
      kafkaTopics.add(kafkaTopic);
    }
    // Return list of POJOs.
    return kafkaTopics;
  }

  public HashMap<String, String> getTopicConfig(AdminClient admin, String topic){
    HashMap<String, String> topicConfigurations = new HashMap<>();
    Collection<ConfigResource> configResource = Collections.singleton(new ConfigResource(ConfigResource.Type.TOPIC, topic));
    DescribeConfigsResult configsResult = admin.describeConfigs(configResource);
    try {
      Config all_configs = (Config) configsResult.all().get().values().toArray()[0];
      Iterator configIterator = all_configs.entries().iterator();
      while (configIterator.hasNext()){
        ConfigEntry currentConfig = (ConfigEntry) configIterator.next();
        String configKey = currentConfig.name();
        String configValue = currentConfig.value();
        topicConfigurations.put(configKey, configValue);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return topicConfigurations;
  }

  public Properties loadConsumerConfigs() {
    Properties configs = new Properties();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    return configs;
  }

}
