package com.ganesh.drycleaning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ganesh.drycleaning.entity.AdminUser;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    java.util.Optional<AdminUser> findByUsername(String username);
}