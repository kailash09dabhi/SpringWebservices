package com.kd.employeeservice;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Configurations {

  @Bean
  public ModelMapper modalMapper() {
    return new ModelMapper();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
