package com.lozuwa.kafkametricsexporter.kafka.service;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.common.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Configuration
public class KafkaClusterServiceImp implements KafkaClusterService {

  @Autowired
  private KafkaAdminService kafkaAdminService;

  @Override
  public int getKafkaBrokers() {
    AdminClient adminClient = kafkaAdminService.getAdminClient();
    int brokers = 0;
    try {
      List<Integer> kafkaBrokers = adminClient.describeCluster().nodes().get().stream().mapToInt(Node::id).boxed().collect(Collectors.toList());
      brokers = kafkaBrokers.size();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    return brokers;
  }

}
