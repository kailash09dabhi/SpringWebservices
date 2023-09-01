package com.kd.employeeservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

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

    @PostMapping(value = "/employees", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeResponse>> allEmployeesWith(@RequestBody EmployeesRequest  employeesRequest) {
        List<EmployeeResponse> employeeResponseList = employeeService.allEmployeesWith(employeesRequest);
        return ResponseEntity.status(HttpStatus.OK).body(employeeResponseList);
    }
}
