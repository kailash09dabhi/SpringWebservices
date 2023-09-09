package com.kd.employeeservice;

public class EmployeesRequest {
  Integer companyId;
  boolean isExperienced;
  private String bloodgroup;

  public Integer getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Integer companyId) {
    this.companyId = companyId;
  }

  public boolean isExperienced() {
    return isExperienced;
  }

  public void setExperienced(boolean experienced) {
    isExperienced = experienced;
  }

  public String getBloodgroup() {
    return bloodgroup;
  }

  public void setBloodgroup(String bloodgroup) {
    this.bloodgroup = bloodgroup;
  }
}
