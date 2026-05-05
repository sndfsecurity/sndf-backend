package com.sndf.backend.service;

import com.sndf.backend.model.Enquiry;
import com.sndf.backend.repository.EnquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sndf.backend.model.SourceType;

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
    
    public List<Enquiry> getEnquiriesBySource(String source) {
        if (source != null && !source.isEmpty()) {
            SourceType sourceEnum = SourceType.valueOf(source.toUpperCase());
            return enquiryRepository.findBySource(sourceEnum);
        }
        return enquiryRepository.findAll();
    }
    
    
    
//    public Enquiry updateStatus(Long id, String status) {
//
//        Enquiry enquiry = enquiryRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Enquiry not found"));
//
//        enquiry.setStatus(status);
//        return enquiryRepository.save(enquiry);
//    }
//    
//    
    
    public Enquiry updateStatus(Long id, String status) {

        Enquiry enquiry = enquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enquiry not found"));

        // 🔒 ADD THIS (IMPORTANT)
        if ("COMPLETED".equalsIgnoreCase(enquiry.getStatus())) {
        	throw new IllegalStateException("Already completed. Cannot modify.");
        }

        enquiry.setStatus(status);
        return enquiryRepository.save(enquiry);
    }
    
    
    public void deleteEnquiry(Long id) {
        enquiryRepository.deleteById(id);
    }
    
}