package com.kd.employeeservice;

public class EmployeeResponse {
    private Integer id;
    private String bloodgroup;
    private String name;
    private String email;
    private CompanyResponse currentCompany;
    private Integer experience;

    public CompanyResponse getCurrentCompany() {
        return currentCompany;
    }

    public void setCurrentCompany(CompanyResponse currentCompany) {
        this.currentCompany = currentCompany;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
