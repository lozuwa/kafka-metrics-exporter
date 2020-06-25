package com.lozuwa.kafkametricsexporter.kafka.service;

import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaConsumerGroup;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@Service
@Configuration
public class KafkaConsumerGroupsServiceImp implements KafkaConsumerGroupService {

  private final static Logger logger = Logger.getLogger(KafkaConsumerGroupsServiceImp.class.getName());

  @Value(value = "${kafka.bootstrapAddress}")
  private String bootstrapAddress;
  @Value(value = "${kafka.consumer.groupId}")
  private String consumerGroupId;

  /**
   * List all the consumer groups in a kafka cluster.
   * @return Hashmap<String, Boolean> = <groupId, isSimpleConsumerGroup>
   */
  @Override
  public List<KafkaConsumerGroup> describeConsumerGroups(AdminClient adminClient){
    // Local variables.
    List<String> kafkaConsumerGroupIds = new ArrayList<>();
    List<KafkaConsumerGroup> kafkaConsumerGroups = new ArrayList<>();
    // List consumer groups.
    final ListConsumerGroupsResult resultsConsumerGroups = adminClient.listConsumerGroups();
    try {
      Collection<ConsumerGroupListing> consumerGroups = resultsConsumerGroups.all().get();
      consumerGroups
          .forEach((result) -> {
            String groupId = result.groupId();
            Boolean isSimpleConsumerGroup = result.isSimpleConsumerGroup();
            logger.info("Consumergroup: " + result.toString());
            kafkaConsumerGroupIds.add(groupId);
          });
    } catch (final InterruptedException | ExecutionException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
    // Describe each consumer group.
    final DescribeConsumerGroupsResult resultsDescribeConsumerGroups = adminClient.describeConsumerGroups(kafkaConsumerGroupIds);
    try {
      Map<String, ConsumerGroupDescription> consumerGroupDescriptions = resultsDescribeConsumerGroups.all().get();
      for (Map.Entry<String, ConsumerGroupDescription> consumerGroupDescriptionEntry : consumerGroupDescriptions.entrySet()){
        ConsumerGroupDescription consumerGroupDescription = consumerGroupDescriptionEntry.getValue();
        // Get consumer group properties.
        String groupId = consumerGroupDescription.groupId();
        String state = consumerGroupDescription.state().toString();
        String coordinator = consumerGroupDescription.coordinator().host();
        String partitionAssignor = consumerGroupDescription.partitionAssignor();
        getConsumerGroupOffsets(adminClient, groupId);
        // Create a KafkaConsumerGroup instance.
        KafkaConsumerGroup kafkaConsumerGroup = new KafkaConsumerGroup(groupId, state, coordinator, partitionAssignor);
        kafkaConsumerGroups.add(kafkaConsumerGroup);
      }
    } catch (final InterruptedException | ExecutionException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
    // Return List<KafkaConsumerGroup>
    return kafkaConsumerGroups;
  }

  public void getConsumerGroupOffsets(AdminClient adminClient, String groupId){
    try {
      // Get consumer group offsets.
      Map<TopicPartition, OffsetAndMetadata> consumerGroupOffsets = adminClient.listConsumerGroupOffsets(groupId).partitionsToOffsetAndMetadata().get();
      // Get topic endoffsets.
      Properties configs = loadConsumerConfigs();
      KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(configs);
      Map<TopicPartition, Long> topicEndOffsets = consumer.endOffsets(consumerGroupOffsets.keySet());
      consumer.close();
      // Compute lag.
      for (Map.Entry<TopicPartition, OffsetAndMetadata> consumerGroupOffset : consumerGroupOffsets.entrySet()){
        consumerGroupOffset.getKey().partition();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
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
