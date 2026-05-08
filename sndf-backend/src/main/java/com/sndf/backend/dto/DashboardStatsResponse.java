package com.sndf.backend.dto;

public class DashboardStatsResponse {

    private long totalEnquiries;
    private long newEnquiries;
    private long inProgress;
    private long completed;

    public DashboardStatsResponse(
            long totalEnquiries,
            long newEnquiries,
            long inProgress,
            long completed
    ) {
        this.totalEnquiries = totalEnquiries;
        this.newEnquiries = newEnquiries;
        this.inProgress = inProgress;
        this.completed = completed;
    }

    public long getTotalEnquiries() {
        return totalEnquiries;
    }

    public long getNewEnquiries() {
        return newEnquiries;
    }

    public long getInProgress() {
        return inProgress;
    }

    public long getCompleted() {
        return completed;
    }
}