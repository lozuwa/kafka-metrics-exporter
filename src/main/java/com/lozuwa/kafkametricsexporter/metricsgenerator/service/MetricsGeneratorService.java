package com.lozuwa.kafkametricsexporter.metricsgenerator.service;

import com.lozuwa.kafkametricsexporter.Utils.Utils;
import com.lozuwa.kafkametricsexporter.kafka.Model.KafkaTopic;
import com.lozuwa.kafkametricsexporter.kafka.service.KafkaService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Service
@Configuration
public class MetricsGeneratorService {

  private final static Logger logger = Logger.getLogger(MetricsGeneratorService.class.getName());

  @Autowired
  private KafkaService kafkaService;

  public MetricsGeneratorService(MeterRegistry meterRegistry){
    // Kafka topic partitions gauge.
    Iterable<Tag> tags = Tags.of("topic", "._internal");
    final AtomicInteger kafkaTopicPartitionsGauge = meterRegistry.gauge("kafka.topic.partitions", tags, new AtomicInteger(0));
  }

  public void generateMetrics(String service) {
    switch (service) {
      case "kafka":
        List<KafkaTopic> kafkaTopics = kafkaService.loadKafkaTopicsToCollection();
        for (KafkaTopic kafkaTopic : kafkaTopics) {
          // Extract data from topic.
          String topicName = kafkaTopic.getTopicName();
          int partitions = kafkaTopic.getPartitions();
          // Load data to partitions gauge.
          Iterable<Tag> tags = Tags.of("topic", topicName);
          AtomicInteger kafkaTopicPartitionsGauge = Metrics.gauge("kafka.topic.partitions", tags, new AtomicInteger(0));
          kafkaTopicPartitionsGauge.set(partitions);
        }
        break;
      case "zookeeper":
        break;
      default:
        logger.info(Utils.StringFormatter("Service {0} not supported.", service));
        break;
    }
  }

}
