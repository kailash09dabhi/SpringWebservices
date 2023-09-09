package com.kd.employeeservice;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

  @Autowired EmployeeService employeeService;

  @GetMapping("/employee/{id}")
  public ResponseEntity<EmployeeResponse> employee(@PathVariable("id") int id) {
    EmployeeResponse employeeResponse = employeeService.getEmployeeById(id);
    return ResponseEntity.status(HttpStatus.OK).body(employeeResponse);
  }

  @GetMapping("/employee/list")
  public ResponseEntity<List<EmployeeResponse>> employees() {
    List<EmployeeResponse> employeeResponseList = employeeService.getEmployees();
    return ResponseEntity.status(HttpStatus.OK).body(employeeResponseList);
  }

  @PostMapping(
      value = "/employees",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<EmployeeResponse>> allEmployeesWith(
      @RequestBody EmployeesRequest employeesRequest) {
    List<EmployeeResponse> employeeResponseList =
        employeeService.allEmployeesWith(employeesRequest);
    return ResponseEntity.status(HttpStatus.OK).body(employeeResponseList);
  }

  @PutMapping("/employee/create")
  public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody Employee employee) {
    EmployeeResponse savedEmployee = employeeService.createEmployee(employee);
    return ResponseEntity.status(HttpStatus.OK).body(savedEmployee);
  }

  @PostMapping("/employee")
  public ResponseEntity<EmployeeResponse> updateEmployee(@RequestBody Employee employee) {
    EmployeeResponse savedEmployee = employeeService.updatedEmployee(employee);
    return ResponseEntity.status(HttpStatus.OK).body(savedEmployee);
  }
}
