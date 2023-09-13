package com.kd.companyservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.kd.common.CompanyDeleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class CompanyProducer {
  private static final Logger LOGGER = LoggerFactory.getLogger(CompanyProducer.class);

  private NewTopic topic;

  private KafkaTemplate<String, CompanyDeleteEvent> kafkaTemplate;

  public CompanyProducer(
      @Qualifier("producer") NewTopic topic,
      KafkaTemplate<String, CompanyDeleteEvent> kafkaTemplate) {
    this.topic = topic;
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendMessage(CompanyDeleteEvent event) {
    LOGGER.info("CompanyDeleteEvent " + event.toString() + " " + topic.name());
    Message<CompanyDeleteEvent> message =
        MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, topic.name()).build();
    kafkaTemplate.send(message);
  }
}
