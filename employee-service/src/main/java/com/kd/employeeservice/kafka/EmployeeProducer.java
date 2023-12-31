package com.kd.employeeservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.kd.common.EmployeeCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeProducer {
  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeProducer.class);
  private NewTopic topic;

  private KafkaTemplate<String, EmployeeCreateEvent> kafkaTemplate;

  public EmployeeProducer(
      @Qualifier("producer") NewTopic topic,
      KafkaTemplate<String, EmployeeCreateEvent> kafkaTemplate) {
    this.topic = topic;
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendMessage(EmployeeCreateEvent event) {
    LOGGER.info("EmployeeCreateEvent " + event.toString());
    Message<EmployeeCreateEvent> message =
        MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, topic.name()).build();
    kafkaTemplate.send(message);
  }
}
