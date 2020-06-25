package com.lozuwa.kafkametricsexporter.kafka.service;

import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaConsumerGroup;
import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaTopic;
import org.apache.kafka.clients.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class KafkaServiceImp implements KafkaService {

  private final static Logger logger = Logger.getLogger(KafkaServiceImp.class.getName());

  @Autowired
  private KafkaAdminService kafkaAdminService;

  @Autowired
  private KafkaClusterService kafkaClusterService;

  @Autowired
  private KafkaTopicService kafkaTopicService;

  @Autowired
  private KafkaConsumerGroupService kafkaConsumerGroupService;

  @Autowired
  public KafkaServiceImp(KafkaAdminService kafkaAdminService, KafkaTopicService kafkaTopicService, KafkaConsumerGroupService kafkaConsumerGroupService, KafkaClusterService kafkaClusterService){
    this.kafkaAdminService = kafkaAdminService;
    this.kafkaTopicService = kafkaTopicService;
    this.kafkaConsumerGroupService = kafkaConsumerGroupService;
    this.kafkaClusterService = kafkaClusterService;
  }

  @Override
  public int getKafkaBrokers(){
    int kafkaBrokers = kafkaClusterService.getKafkaBrokers();
    return kafkaBrokers;
  }

  @Override
  public List<KafkaTopic> loadKafkaTopicsToCollection(){
    AdminClient adminClient = this.kafkaAdminService.getAdminClient();
    List<KafkaTopic> kafkaTopics = kafkaTopicService.describeTopics(adminClient);
    adminClient.close();
    return kafkaTopics;
  }

  @Override
  public List<KafkaConsumerGroup> loadKafkaConsumerGroupsToCollection(){
    AdminClient adminClient = this.kafkaAdminService.getAdminClient();
    List<KafkaConsumerGroup> kafkaConsumerGroups = kafkaConsumerGroupService.describeConsumerGroups(adminClient);
    adminClient.close();
    return kafkaConsumerGroups;
  }

}
