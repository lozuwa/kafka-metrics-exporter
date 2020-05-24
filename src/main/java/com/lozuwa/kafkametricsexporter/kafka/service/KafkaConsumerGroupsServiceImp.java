package com.lozuwa.kafkametricsexporter.kafka.service;

import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaConsumerGroup;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@Service
@Configuration
public class KafkaConsumerGroupsServiceImp implements KafkaConsumerGroupService {

  private final static Logger logger = Logger.getLogger(KafkaConsumerGroupsServiceImp.class.getName());

  /**
   * List all the consumer groups in a kafka cluster.
   * @return Hashmap<String, Boolean> = <groupId, isSimpleConsumerGroup>
   */
  @Override
  public List<KafkaConsumerGroup> describeConsumerGroups(AdminClient admin){
    // Local variables.
    List<String> kafkaConsumerGroupIds = new ArrayList<>();
    List<KafkaConsumerGroup> kafkaConsumerGroups = new ArrayList<>();
    // List consumer groups.
    final ListConsumerGroupsResult results = admin.listConsumerGroups();
    try {
      results
          .all()
          .get()
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
    final DescribeConsumerGroupsResult resultsDescribe = admin.describeConsumerGroups(kafkaConsumerGroupIds);
    try {
      resultsDescribe
          .all()
          .get()
          .forEach((key, value) -> {
            // Get values to create an instance of KafkaConsumerGroup.
            String groupId = value.groupId();
            String state = value.state().toString();
            String coordinator = value.coordinator().host();
            String partitionAssignor = value.partitionAssignor();
            // Create instance of KafkaConsumerGroup.
            KafkaConsumerGroup kafkaConsumerGroup = new KafkaConsumerGroup(groupId, state, coordinator, partitionAssignor);
            kafkaConsumerGroups.add(kafkaConsumerGroup);
          });
    } catch (final InterruptedException | ExecutionException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
    // Return List<KafkaConsumerGroup>
    return kafkaConsumerGroups;
  }

}
