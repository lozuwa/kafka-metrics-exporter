package com.lozuwa.kafkametricsexporter.metricsgenerator.service;

import com.lozuwa.kafkametricsexporter.Utils.KafkaTopicMetricNames;
import com.lozuwa.kafkametricsexporter.Utils.Utils;
import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaConsumerGroup;
import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaTopic;
import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaTopicPartition;
import com.lozuwa.kafkametricsexporter.kafka.service.KafkaService;
import io.micrometer.core.instrument.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
@Configuration
public class MetricsGeneratorService {

  private final static Logger logger = Logger.getLogger(MetricsGeneratorService.class.getName());

  @Autowired
  private KafkaService kafkaService;

  private Map<String, Integer> clusterGaugeCache = new HashMap<>();
  private Map<String, KafkaTopic> topicGaugeCache = new HashMap<>();

  public MetricsGeneratorService(MeterRegistry meterRegistry){
  }

  public void generateMetrics(String service) {
    switch (service) {
      case "kafka":
        // BROKER
        int kafkaBrokers = kafkaService.getKafkaBrokers();
        clusterGaugeCache.put("kafka.brokers", kafkaBrokers);
        loadKafkaBrokersGauge();

        // TOPICS
        List<KafkaTopic> kafkaTopics = kafkaService.loadKafkaTopicsToCollection();
        for (KafkaTopic kafkaTopic : kafkaTopics) {
          // Extract data from the topic.
          String topicName = kafkaTopic.getTopicName();
          List<Tag> tags = joinTopicTags(kafkaTopic.getTopicConfigurations());
          // Save value in a strong reference object that acts as a cache.
          topicGaugeCache.put(topicName, kafkaTopic);
          // Load partitions gauge.
          loadPartitionsGauge(kafkaTopic, tags);
          // Load replication gauge.
          loadReplicationFactorGauge(kafkaTopic, tags);
          // Load in sync replicas gauge.
          loadPartitionInSyncReplicasGauge(kafkaTopic, tags);
          // Load partition leader gauge.
          loadPartitionLeaderGauge(kafkaTopic, tags);
          // Load under replicated partition gauge.
          loadPartitionUnderReplicatedPartition(kafkaTopic, tags);
        }

        // CONSUMER GROUPS
        List<KafkaConsumerGroup> kafkaConsumerGroups = kafkaService.loadKafkaConsumerGroupsToCollection();
        for (KafkaConsumerGroup kafkaConsumerGroup : kafkaConsumerGroups){
          // Extract data from the consumer group.
          String consumerGroupId = kafkaConsumerGroup.getId();
        }

        break;
      case "zookeeper":
        break;
      default:
        logger.info(Utils.StringFormatter("Service {0} not supported.", service));
        break;
    }
  }

  public List<Tag> joinTopicTags(HashMap<String, String> topicConfigurations){
    List<Tag> tags = new ArrayList<>();
    tags.add(Tag.of("job", "kafka"));
    for (Map.Entry<String, String> topicConfiguration : topicConfigurations.entrySet()){
      String key = topicConfiguration.getKey();
      String value = topicConfiguration.getValue();
      tags.add(Tag.of(key, value));
    }
    return tags;
  }

  public void loadKafkaBrokersGauge(){
    // Load tags.
    List<Tag> tags = new ArrayList<>();
    tags.add(Tag.of("job", "kafka"));
    // Load cluster gauge.
    Metrics.gauge(KafkaTopicMetricNames.KAFKA_BROKERS,
        tags,
        clusterGaugeCache,
        g -> g.get("kafka.brokers"));
  }

  public void loadPartitionsGauge(KafkaTopic kafkaTopic, List<Tag> tags){
    // Extract name.
    String topicName = kafkaTopic.getTopicName();
    // Load tags.
    tags.add(Tag.of("topic.name", topicName));
    // Load partitions gauge.
    Metrics.gauge(KafkaTopicMetricNames.KAFKA_TOPIC_PARTITIONS,
        tags,
        topicGaugeCache,
        g -> g.get(topicName).getPartitions().size());
  }

  public void loadReplicationFactorGauge(KafkaTopic kafkaTopic, List<Tag> tags){
    String topicName = kafkaTopic.getTopicName();
    List<KafkaTopicPartition> partitions = kafkaTopic.getPartitions();
    for (int index = 0; index < partitions.size(); index++) {
      KafkaTopicPartition partition =  partitions.get(index);
      int partitionId = partition.getPartitionId();
      // Load tags.
      tags.add(Tag.of("topic.name", topicName));
      tags.add(Tag.of("topic.partition", String.valueOf(partitionId)));
      // Load replication factor gauge for each partition.
      final int indexMap = index;
      Metrics.gauge(KafkaTopicMetricNames.KAFKA_TOPIC_REPLICATION_FACTOR,
          tags,
          topicGaugeCache,
          g -> g.get(topicName).getPartitions().get(indexMap).getReplicationFactor()
      );
    }
  }

  public void loadPartitionInSyncReplicasGauge(KafkaTopic kafkaTopic, List<Tag> tags){
    String topicName = kafkaTopic.getTopicName();
    List<KafkaTopicPartition> partitions = kafkaTopic.getPartitions();
    for (int index = 0; index < partitions.size(); index++) {
      KafkaTopicPartition partition =  partitions.get(index);
      int partitionId = partition.getPartitionId();
      // Load tags.
      tags.add(Tag.of("topic.name", topicName));
      tags.add(Tag.of("topic.partition", String.valueOf(partitionId)));
      // Load replication factor gauge for each partition.
      final int indexMap = index;
      Metrics.gauge(KafkaTopicMetricNames.KAFKA_TOPIC_PARTITION_IN_SYNC_REPLICA,
          tags,
          topicGaugeCache,
          g -> g.get(topicName).getPartitions().get(indexMap).getInSyncReplicas()
      );
    }
  }

  public void loadPartitionLeaderGauge(KafkaTopic kafkaTopic, List<Tag> tags){
    String topicName = kafkaTopic.getTopicName();
    List<KafkaTopicPartition> partitions = kafkaTopic.getPartitions();
    for (int index = 0; index < partitions.size(); index++) {
      KafkaTopicPartition partition =  partitions.get(index);
      int partitionId = partition.getPartitionId();
      // Load tags.
      tags.add(Tag.of("topic.name", topicName));
      tags.add(Tag.of("topic.partition", String.valueOf(partitionId)));
      // Load replication factor gauge for each partition.
      final int indexMap = index;
      Metrics.gauge(KafkaTopicMetricNames.KAFKA_TOPIC_PARTITION_LEADER,
          tags,
          topicGaugeCache,
          g -> g.get(topicName).getPartitions().get(indexMap).getLeaderId()
      );
    }
  }

  public void loadPartitionUnderReplicatedPartition(KafkaTopic kafkaTopic, List<Tag> tags){
    String topicName = kafkaTopic.getTopicName();
    List<KafkaTopicPartition> partitions = kafkaTopic.getPartitions();
    for (int index = 0; index < partitions.size(); index++) {
      KafkaTopicPartition partition =  partitions.get(index);
      int partitionId = partition.getPartitionId();
      // Load tags.
      tags.add(Tag.of("topic.name", topicName));
      tags.add(Tag.of("topic.partition", String.valueOf(partitionId)));
      // Load replication factor gauge for each partition.
      final int indexMap = index;
      Metrics.gauge(KafkaTopicMetricNames.KAFKA_TOPIC_PARTITION_UNDER_REPLICATED_PARTITION,
          tags,
          topicGaugeCache,
          g -> g.get(topicName).getPartitions().get(indexMap).getOfflineReplicas()
      );
    }
  }

}
