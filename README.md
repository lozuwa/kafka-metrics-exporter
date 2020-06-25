# TODO
* Consumer groups must have in its POJO Map<Topic, List<Partitons>>
* Partitions must have partitionId, currentOffset, endOffset 

# Kafka-metrics-exporter
## Description
A spring boot implementation of a kafka exporter for prometheus.

## Environment variables 
* CONSUL_APPLICATION_NAME
* CONSUL_HOST
* CONSUL_PORT
* CONSUL_ACL_TOKEN
* CONSUL_ENABLED
* CONSUL_PREFIX

## Consul
Configuration variables are listed below.

### Kafka
* kafka.bootstrapAddress
* kafka.consumer.groupId

### References
* https://gquintana.github.io/2020/01/16/Retrieving-Kafka-lag.html

