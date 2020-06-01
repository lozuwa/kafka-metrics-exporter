package com.lozuwa.kafkametricsexporter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaMetricsExporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaMetricsExporterApplication.class, args);
	}

}
