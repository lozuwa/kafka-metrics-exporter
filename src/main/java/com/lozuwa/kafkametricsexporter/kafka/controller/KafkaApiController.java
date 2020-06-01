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

  @Autowired
  MeterRegistry meterRegistry;

  public KafkaApiController(MeterRegistry meterRegistry){
    // Counter.
    final Counter myCounter = meterRegistry.counter("my.counter", "keycounter", "valuecounter");
    // Gauge.
    Iterable<Tag> iterable = Tags.of("keygauge", "valuegauge");
    final AtomicInteger myGauge = meterRegistry.gauge("my.gauge", iterable, new AtomicInteger(0));
  }

  @RequestMapping(value="/test", method=RequestMethod.GET)
  public ResponseEntity<String> test(){
    // Generate a random string for the tags.
    String[] abc = {"a", "b", "c"};
    Random rand = new Random();
    StringBuilder randomValue = new StringBuilder();
    for (int index=0; index<=1; index++){
      int randIndex = rand.nextInt(abc.length-1);
      randomValue.append(abc[randIndex]);
    }

    // Update already declared counter. Add tag value and counter.
    Counter counter = Metrics.counter("my.counter",  "keycounter", randomValue.toString());
    counter.increment();

    // Update already declared gauge. Add tag value and set quantity.
    Iterable<Tag> iterable = Tags.of("keygauge", randomValue.toString());
    AtomicInteger gauge = Metrics.gauge("my.gauge", iterable, new AtomicInteger(0));
    int randNumber = rand.nextInt(abc.length-1);
    gauge.set(randNumber);

    return new ResponseEntity<>("TEST", HttpStatus.OK);
  }

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
