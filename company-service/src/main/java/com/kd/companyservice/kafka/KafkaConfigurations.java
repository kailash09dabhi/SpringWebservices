package com.kd.companyservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfigurations {
  @Value("${kd.kafka.consumer.topic.name}")
  private String topicName;

  @Value("${kd.kafka.producer.topic.name}")
  private String topicNameProducer;

  @Bean(name = "consumer")
  public NewTopic topic() {
    return TopicBuilder.name(topicName).build();
  }

  @Bean(name = "producer")
  public NewTopic topicProducer() {
    return TopicBuilder.name(topicNameProducer).build();
  }
}
