package com.kd.employeeservice.kafka;

import com.kd.employeeservice.CompanyResponse;
import com.kd.employeeservice.EmployeeService;
import org.kd.common.CompanyDeleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CompanyConsumer {
  private static final Logger LOGGER = LoggerFactory.getLogger(CompanyConsumer.class);

  @Autowired EmployeeService employeeService;
  @Autowired RestTemplate restTemplate;

  @KafkaListener(
      topics = "${kd.kafka.consumer.topic.name}",
      groupId = "${spring.kafka.consumer.group-id}")
  public void consume(CompanyDeleteEvent event) {
    LOGGER.info("Employee Service --- Company consumer");
    employeeService.deleteAllEmployeesOfCompany(event.getId());
    //    employeeService.deleteCompany(event.getId());
  }
}
