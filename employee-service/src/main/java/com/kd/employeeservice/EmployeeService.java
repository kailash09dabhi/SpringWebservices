package com.kd.employeeservice;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
    private static final String EMPLOYEE_TABLE = "employee";
    private static final String COMPANY_TABLE = "company";
    private static final char SPACE = ' ';
    RestTemplate restTemplate;
    @Autowired
    SessionFactory sessionFactory;
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
        employeeResponse.setCurrentCompany(restTemplate.getForObject("/company/{id}", CompanyResponse.class, employee.getCurrentCompanyId()));
        return employeeResponse;
    }

    public List<EmployeeResponse> getEmployees() {
        List<Employee> employees = employeeRepo.findAll();
        List<EmployeeResponse> employeeResponseList = employees.stream().map(emp -> modelMapper.map(emp, EmployeeResponse.class)).toList();
        employeeResponseList.stream().filter(it -> it.getCurrentCompany() != null).forEach(empRes -> empRes.setCurrentCompany(restTemplate.getForObject("/company/{id}", CompanyResponse.class, empRes.getCurrentCompany().getId())));
        return employeeResponseList;
    }

    public List<EmployeeResponse> allEmployeesWith(EmployeesRequest employeesRequest) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select * from" + SPACE + EMPLOYEE_TABLE + SPACE + "e" + SPACE);
        List<Object> employeeList = new ArrayList<>();
        try {
            if (employeesRequest.getBloodgroup() != null) {
                queryBuilder.append("left join company c on e.current_company_id=c.id where e.bloodgroup='").append(employeesRequest.getBloodgroup()).append("';");
                EntityManagerFactory entityManagerFactory = sessionFactory.createEntityManager().getEntityManagerFactory();
                EntityManager entityManager = entityManagerFactory.createEntityManager();
                Query query = entityManager.createNativeQuery(queryBuilder.toString());
                employeeList.addAll(query.getResultList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
        List<Employee> employees = employeeList.stream().map(emp -> {
            Object[] arr = (Object[]) emp;
            Employee employee = new Employee();
            employee.setId((Integer) arr[0]);
            employee.setName((String) arr[1]);
            employee.setEmail((String) arr[2]);
            employee.setBloodgroup((String) arr[3]);
            employee.setCurrentCompanyId(arr[4] == null ? null : (Integer) arr[4]);
            employee.setExperience((Integer) arr[5]);
            return employee;
        }).toList();
        List<EmployeeResponse> employeeResponseList = employees.stream().map(emp -> modelMapper.map(emp, EmployeeResponse.class)).toList();
        employeeResponseList.stream().filter(it -> it.getCurrentCompany() != null).forEach(empRes -> empRes.setCurrentCompany(restTemplate.getForObject("/company/{id}", CompanyResponse.class, empRes.getCurrentCompany().getId())));
        return employeeResponseList;
    }

    public EmployeeResponse createEmployee(Employee employee) {
        Employee newCreatedEmp = employeeRepo.saveAndFlush(employee);
        EmployeeResponse employeeResponse = modelMapper.map(newCreatedEmp, EmployeeResponse.class);
        employeeResponse.setCurrentCompany(restTemplate.getForObject("/company/{id}", CompanyResponse.class, employee.getCurrentCompanyId()));
        return employeeResponse;
    }

    public EmployeeResponse updatedEmployee(Employee employee) {
        Employee emp = employeeRepo.getReferenceById(employee.getId());
        emp.setName(employee.getName());
        emp.setEmail(employee.getEmail());
        emp.setCurrentCompanyId(employee.getCurrentCompanyId());
        emp.setBloodgroup(employee.getBloodgroup());
        emp.setExperience(employee.getExperience());
        Employee updatedEmp = employeeRepo.saveAndFlush(employee);
        EmployeeResponse employeeResponse = modelMapper.map(updatedEmp, EmployeeResponse.class);
        employeeResponse.setCurrentCompany(restTemplate.getForObject("/company/{id}", CompanyResponse.class, employee.getCurrentCompanyId()));
        return employeeResponse;
    }
}
