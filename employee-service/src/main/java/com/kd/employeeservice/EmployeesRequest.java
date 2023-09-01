package com.kd.employeeservice;

public class EmployeesRequest {
    private String bloodgroup;
    Integer companyId;
    boolean isExperienced;

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
