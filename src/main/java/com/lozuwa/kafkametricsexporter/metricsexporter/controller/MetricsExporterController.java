package com.lozuwa.kafkametricsexporter.metricsexporter.controller;

import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaConsumerGroup;
import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaTopic;
import com.lozuwa.kafkametricsexporter.kafka.service.KafkaService;
import com.lozuwa.kafkametricsexporter.metricsexporter.service.MetricsExporterService;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/metrics")
public class MetricsExporterController {

  private final static Logger logger = Logger.getLogger(MetricsExporterController.class.getName());

  @Autowired
  private KafkaService kafkaService;

  @Autowired
  private MetricsExporterService metricsExporterService;

  public MetricsExporterController(MetricsExporterService metricsExporterService, KafkaService kafkaService){
    this.metricsExporterService = metricsExporterService;
    this.kafkaService = kafkaService;
  }

  @RequestMapping(value = "/topics", method = RequestMethod.GET)
  public ResponseEntity<List<KafkaTopic>> metricsTopics(){
    List<KafkaTopic> compactedKafkaTopics = metricsExporterService.getMetricsFromCompactedTopics();
    return new ResponseEntity<>(compactedKafkaTopics, HttpStatus.OK);
  }

  @RequestMapping(value = "/consumerGroups", method = RequestMethod.GET)
  public ResponseEntity<List<KafkaConsumerGroup>> metricsConsumerGroups(){
    List<KafkaConsumerGroup> kafkaConsumerGroups = metricsExporterService.getMetricsFromConsumerGroupIds();
    return new ResponseEntity<>(kafkaConsumerGroups, HttpStatus.OK);
  }

}
