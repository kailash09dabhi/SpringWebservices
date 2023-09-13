package com.kd.companyservice.kafka;

import com.kd.companyservice.Company;
import com.kd.companyservice.CompanyService;
import org.kd.common.EmployeeCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmployeeConsumer {
  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeConsumer.class);
  @Autowired CompanyService companyService;

  @KafkaListener(
      topics = "${kd.kafka.consumer.topic.name}",
      groupId = "${spring.kafka.consumer.group-id}")
  public void consume(EmployeeCreateEvent employeeCreateEvent) {
    LOGGER.info(
        "Employee create event received in company service, so lets create new company if company is not provided for the employee "
            + employeeCreateEvent.toString());
    Company company =
        companyService.getCompanyId(employeeCreateEvent.getEmployee().getCurrentCompanyId());
    if (company != null && company.getId() == 5) {
      Company newCompany = new Company();
      newCompany.setName(employeeCreateEvent.getEmployee().getName() + " pvt Ltd");
      newCompany.setAddress("home");
      companyService.create(newCompany);
    }
  }
}
