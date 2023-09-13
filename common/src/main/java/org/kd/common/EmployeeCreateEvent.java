package org.kd.common;

public class EmployeeCreateEvent {
  private String message;
  private EmployeeDTO employee;

  public EmployeeCreateEvent() {}

  public EmployeeCreateEvent(String message, EmployeeDTO employee) {
    this.message = message;
    this.employee = employee;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public EmployeeDTO getEmployee() {
    return employee;
  }

  public void setEmployee(EmployeeDTO employee) {
    this.employee = employee;
  }
}
