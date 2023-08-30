package com.kd.employeeservice;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EmployeeService {
    RestTemplate restTemplate;
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private ModelMapper modelMapper;

    EmployeeService(@Value("${company.service.base.url}") String companyServiceBaseUrl, RestTemplateBuilder builder) {
        this.restTemplate = builder.rootUri(companyServiceBaseUrl).build();
    }

    public EmployeeResponse getEmployeeById(Integer id) {
        Employee employee = employeeRepo.getReferenceById(id);
        EmployeeResponse employeeResponse = modelMapper.map(employee, EmployeeResponse.class);
        employeeResponse.setCurrentCompany(restTemplate.getForObject("/{id}", CompanyResponse.class, employee.getCurrentCompanyId()));
        return employeeResponse;
    }

    public List<EmployeeResponse> getEmployees() {
        List<Employee> employees = employeeRepo.findAll();
        List<EmployeeResponse> employeeResponseList = employees.stream().map(emp -> modelMapper.map(emp, EmployeeResponse.class)).toList();
        employeeResponseList.stream().filter(it -> it.getCurrentCompany() != null).forEach(empRes -> empRes.setCurrentCompany(restTemplate.getForObject("/{id}", CompanyResponse.class, empRes.getCurrentCompany().getId())));
        return employeeResponseList;
    }
}
