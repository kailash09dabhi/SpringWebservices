package com.kd.companyservice;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {
    @Autowired
    CompanyService companyService;
    @Autowired
    ModelMapper modelMapper;
    @GetMapping("/company/{id}")
    public ResponseEntity<CompanyResponse> employee(@PathVariable("id") int id) {
        Company company = companyService.getCompanyId(id);
        CompanyResponse companyResponse =  modelMapper.map(company,CompanyResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(companyResponse);
    }

}
