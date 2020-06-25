package com.lozuwa.kafkametricsexporter.kafka.service;

import org.apache.kafka.clients.admin.AdminClient;

public interface KafkaAdminService {

   public AdminClient getAdminClient();

   public void closeAdminClient(AdminClient adminClient);

}
