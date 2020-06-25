package com.lozuwa.kafkametricsexporter.kafka.controller;

import com.lozuwa.kafkametricsexporter.Utils.Utils;
import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaConsumerGroup;
import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaTopic;
import com.lozuwa.kafkametricsexporter.kafka.service.KafkaService;
import io.micrometer.core.instrument.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@RestController
@RequestMapping(value="/v1/api/")
public class KafkaApiController {

  private final static Logger logger = Logger.getLogger(KafkaApiController.class.getName());

  @Autowired
  KafkaService kafkaService;

  @RequestMapping(value="/topics", method=RequestMethod.GET)
  public ResponseEntity<List<KafkaTopic>> getTopics(){
    // Get topics from kafka.
    List<KafkaTopic> kafkaTopics = kafkaService.loadKafkaTopicsToCollection();
    // Response.
    return new ResponseEntity<>(kafkaTopics, HttpStatus.OK);
  }

  @RequestMapping(value="/consumerGroups", method=RequestMethod.GET)
  public ResponseEntity<List<KafkaConsumerGroup>> getConsumerGroups(){
    // Get consumer groups from kafka.
    List<KafkaConsumerGroup> kafkaConsumerGroups = kafkaService.loadKafkaConsumerGroupsToCollection();
    // Response.
    return new ResponseEntity<>(kafkaConsumerGroups, HttpStatus.OK);
  }

}
