package com.lozuwa.kafkametricsexporter.metricsgenerator.controller;

import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaTopic;
import com.lozuwa.kafkametricsexporter.kafka.controller.KafkaApiController;
import com.lozuwa.kafkametricsexporter.kafka.service.KafkaService;
import com.lozuwa.kafkametricsexporter.metricsgenerator.service.MetricsGeneratorService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@RestController
@RequestMapping(value="/v1/api/")
public class MetricsGeneratorController {

  private final static Logger logger = Logger.getLogger(MetricsGeneratorController.class.getName());

  @Autowired
  private MetricsGeneratorService metricsGeneratorService;

  @RequestMapping(value="/trigger/metrics/{service}", method=RequestMethod.GET)
  public ResponseEntity<String> triggerServiceMetrics(@PathVariable("service") String service){
    metricsGeneratorService.generateMetrics(service);
    return new ResponseEntity<>("OK", HttpStatus.OK);
  }

}
