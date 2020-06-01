package com.lozuwa.kafkametricsexporter.metricsgenerator.task;

import com.lozuwa.kafkametricsexporter.metricsgenerator.controller.MetricsGeneratorController;
import com.lozuwa.kafkametricsexporter.metricsgenerator.service.MetricsGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class ServiceScrapper {

  private final static Logger logger = Logger.getLogger(MetricsGeneratorController.class.getName());

  @Autowired
  private MetricsGeneratorService metricsGeneratorService;

  @Scheduled(fixedDelayString = "${scrapper.kafka.service.fixedDelay}", initialDelay = 10000)
  public void scrapeKafkaService() {
    logger.info("Scrapping kafka configuration.");
    metricsGeneratorService.generateMetrics("kafka");
  }

}
