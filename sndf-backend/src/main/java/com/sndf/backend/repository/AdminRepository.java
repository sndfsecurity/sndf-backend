package com.sndf.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sndf.backend.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByEmail(String email);
}