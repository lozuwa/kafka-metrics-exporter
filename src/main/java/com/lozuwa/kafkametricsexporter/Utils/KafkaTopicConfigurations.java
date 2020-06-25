package com.lozuwa.kafkametricsexporter.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KafkaTopicConfigurations {

  private static List<String> topicConfigurations = new ArrayList<>();

  public static List<String> newKafkaTopicConfigurations() {
    topicConfigurations.add("compression.type");
    topicConfigurations.add("leader.replication.throttled.replicas");
    topicConfigurations.add("min.insync.replicas");
    topicConfigurations.add("message.downconversion.enable");
    topicConfigurations.add("segment.jitter.ms");
    topicConfigurations.add("cleanup.policy");
    topicConfigurations.add("flush.ms");
    topicConfigurations.add("follower.replication.throttled.replicas");
    topicConfigurations.add("segment.bytes");
    topicConfigurations.add("retention.ms");
    topicConfigurations.add("flush.messages");
    topicConfigurations.add("message.format.version");
    topicConfigurations.add("max.compaction.lag.ms");
    topicConfigurations.add("file.delete.delay.ms");
    topicConfigurations.add("max.message.bytes");
    topicConfigurations.add("min.compaction.lag.ms");
    topicConfigurations.add("message.timestamp.type");
    topicConfigurations.add("preallocate");
    topicConfigurations.add("index.interval.bytes");
    topicConfigurations.add("min.cleanable.dirty.ratio");
    topicConfigurations.add("unclean.leader.election.enable");
    topicConfigurations.add("retention.bytes");
    topicConfigurations.add("delete.retention.ms");
    topicConfigurations.add("segment.ms");
    topicConfigurations.add("message.timestamp.difference.max.ms");
    topicConfigurations.add("segment.index.bytes");
    return topicConfigurations;
  }

}
