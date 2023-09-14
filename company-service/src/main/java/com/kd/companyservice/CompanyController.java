package com.kd.companyservice;

import com.kd.companyservice.kafka.CompanyProducer;
import java.util.Date;
import java.util.List;
import org.kd.common.CompanyDeleteEvent;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompanyController {
  @Autowired CompanyService companyService;
  @Autowired ModelMapper modelMapper;
  @Autowired CompanyProducer companyProducer;

  @GetMapping("/company/{id}")
  public ResponseEntity<CompanyResponse> employee(@PathVariable("id") int id) {
    Company company = companyService.getCompanyId(id);
    CompanyResponse companyResponse = modelMapper.map(company, CompanyResponse.class);
    return ResponseEntity.status(HttpStatus.OK).body(companyResponse);
  }

  @GetMapping("/company/list")
  public ResponseEntity<List<Company>> companies() {
    List<Company> companyList = companyService.companies();
    return ResponseEntity.status(HttpStatus.OK).body(companyList);
  }

  @PostMapping("/company")
  public ResponseEntity<Company> updateCompany(@RequestBody Company company) {
    return ResponseEntity.status(HttpStatus.OK).body(companyService.updatedCompany(company));
  }

  @DeleteMapping("/company/{id}")
  public ResponseEntity<SuccessMessage> deleteCompany(@PathVariable("id") int id) {
    CompanyDeleteEvent event = new CompanyDeleteEvent();
    event.setId(id);
    companyProducer.sendMessage(event);
    SuccessMessage successMessage =
        new SuccessMessage(
            HttpStatus.OK.value(),
            new Date(),
            "Informed the employee service to remove all employees of Company with id=" + id,
            "");
    return ResponseEntity.status(HttpStatus.OK).body(successMessage);
  }

  public record SuccessMessage(
      int statusCode, Date timestamp, String message, String description) {}
}
