package com.kd.employeeservice;

import jakarta.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "bloodgroup")
    private String bloodgroup;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;

    @Column(name = "current_company_id")
    private Integer currentCompanyId;
    @Column(name = "experience")
    private Integer experience;

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

    public Integer getCurrentCompanyId() {
        return currentCompanyId;
    }

    public void setCurrentCompanyId(Integer currentCompanyId) {
        this.currentCompanyId = currentCompanyId;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }
}
