package com.sndf.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class LoginAttempt {

    @Id
    private String email;

    private int attemptCount;
    private long blockTime;

    // getters & setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAttemptCount() { return attemptCount; }
    public void setAttemptCount(int attemptCount) { this.attemptCount = attemptCount; }

    public long getBlockTime() { return blockTime; }
    public void setBlockTime(long blockTime) { this.blockTime = blockTime; }
}