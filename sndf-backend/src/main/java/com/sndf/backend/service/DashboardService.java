package com.sndf.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sndf.backend.dto.DashboardStatsResponse;
import com.sndf.backend.repository.EnquiryRepository;

@Service
public class DashboardService {

    @Autowired
    private EnquiryRepository enquiryRepository;

    public DashboardStatsResponse getDashboardStats() {

        long total = enquiryRepository.count();

        long newCount = enquiryRepository.countByStatus("NEW");

        long inProgress = enquiryRepository.countByStatus("IN_PROGRESS");

        long completed = enquiryRepository.countByStatus("COMPLETED");

        return new DashboardStatsResponse(
                total,
                newCount,
                inProgress,
                completed
        );
    }
}