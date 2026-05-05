package com.sndf.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sndf.backend.model.LoginAttempt;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, String> {
}