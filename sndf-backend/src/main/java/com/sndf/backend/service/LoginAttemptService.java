package com.sndf.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sndf.backend.model.LoginAttempt;
import com.sndf.backend.repository.LoginAttemptRepository;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPT = 5;
    private static final long BLOCK_TIME = 5 * 60 * 1000;

    @Autowired
    private LoginAttemptRepository repo;

    public void loginFailed(String email) {

        LoginAttempt attempt = repo.findById(email).orElse(new LoginAttempt());
        attempt.setEmail(email);

        int newCount = attempt.getAttemptCount() + 1;
        attempt.setAttemptCount(newCount);

        if (newCount == MAX_ATTEMPT) {
            attempt.setBlockTime(System.currentTimeMillis());
        }

        repo.save(attempt);
    }

    public void loginSucceeded(String email) {
        repo.deleteById(email);
    }

    public boolean isBlocked(String email) {

        LoginAttempt attempt = repo.findById(email).orElse(null);

        if (attempt == null) return false;

        if (attempt.getAttemptCount() >= MAX_ATTEMPT) {

            long diff = System.currentTimeMillis() - attempt.getBlockTime();

            if (diff > BLOCK_TIME) {
                repo.deleteById(email);
                return false;
            }

            return true;
        }

        return false;
    }
}