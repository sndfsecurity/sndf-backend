package com.sndf.backend.service;

import com.sndf.backend.model.Enquiry;
import com.sndf.backend.repository.EnquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnquiryService {

    @Autowired
    private EnquiryRepository enquiryRepository;

    // ✅ Save enquiry
    public Enquiry saveEnquiry(Enquiry enquiry) {
        return enquiryRepository.save(enquiry);
    }

    // ✅ Get all enquiries (latest first)
    public List<Enquiry> getAllEnquiries() {
        return enquiryRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}