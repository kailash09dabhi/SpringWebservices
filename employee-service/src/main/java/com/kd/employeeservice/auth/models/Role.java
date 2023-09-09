package com.kd.employeeservice.auth.models;

import com.kd.employeeservice.auth.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "role")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private RoleType name;

  public Role() {}

  public Role(RoleType name) {
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public RoleType getName() {
    return name;
  }

  public void setName(RoleType name) {
    this.name = name;
  }
}
