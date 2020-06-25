package com.lozuwa.kafkametricsexporter.kafka.service;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.logging.Logger;

@Service
@Configuration
public class KafkaAdminServiceImp implements KafkaAdminService {

  private final static Logger logger = Logger.getLogger(KafkaAdminServiceImp.class.getName());

  @Value(value="${kafka.bootstrapAddress}")
  private String bootstrapAddress;

  @Override
  public AdminClient getAdminClient(){
    logger.info("Generating new kafka client.");
    Properties configs = loadBootstrapConfigs();
    AdminClient adminClient = AdminClient.create(configs);
    return adminClient;
  }

  @Override
  public void closeAdminClient(AdminClient adminClient){
    adminClient.close();
  }

  public Properties loadBootstrapConfigs(){
    Properties configs = new Properties();
    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    return configs;
  }

}
