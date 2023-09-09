package com.kd.employeeservice.auth.repository;

import com.kd.employeeservice.auth.RoleType;
import com.kd.employeeservice.auth.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
