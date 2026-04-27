package com.sndf.backend.service;

import com.sndf.backend.model.Enquiry;
import com.sndf.backend.repository.EnquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnquiryService {

    @Autowired
    private EnquiryRepository enquiryRepository;

    // Save enquiry
    public Enquiry saveEnquiry(Enquiry enquiry) {
        return enquiryRepository.save(enquiry);
    }

    // Get all enquiries (for future admin panel)
    public java.util.List<Enquiry> getAllEnquiries() {
        return enquiryRepository.findAll();
    }
}