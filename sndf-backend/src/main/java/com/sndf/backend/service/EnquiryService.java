package com.sndf.backend.service;

import com.sndf.backend.model.Enquiry;


import com.sndf.backend.repository.EnquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sndf.backend.model.SourceType;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class EnquiryService {

    @Autowired
    private EnquiryRepository enquiryRepository;
    
    
    public Page<Enquiry> getPaginatedEnquiries(
            int page,
            int size,
            String source
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt").descending()
                );

        if (source != null && !source.isEmpty()) {

            SourceType sourceEnum =
                    SourceType.valueOf(
                            source.toUpperCase()
                    );

            return enquiryRepository
                    .findBySource(
                            sourceEnum,
                            pageable
                    );
        }

        return enquiryRepository.findAll(pageable);
    }
    

    // ✅ Save enquiry
//    public Enquiry saveEnquiry(Enquiry enquiry) {
//        return enquiryRepository.save(enquiry);
//    }
//    
    
    
    
 // ✅ Save enquiry
    
    public Enquiry saveEnquiry(Enquiry enquiry) {

        LocalDateTime last24Hours =
                LocalDateTime.now().minusHours(24);

        // QUICK ENQUIRY
        if (enquiry.getSource() == SourceType.QUICK) {

            boolean alreadySubmitted =
                    enquiryRepository
                            .existsByPhoneAndSourceAndCreatedAtAfter(
                                    enquiry.getPhone(),
                                    SourceType.QUICK,
                                    last24Hours
                            );

            if (alreadySubmitted) {
                throw new RuntimeException(
                        "You have already submitted a Quick Enquiry today."
                );
            }
        }

        // CONTACT FORM
        if (enquiry.getSource() == SourceType.CONTACT) {

            boolean alreadySubmitted =
                    enquiryRepository
                            .existsByPhoneAndServiceAndSourceAndCreatedAtAfter(
                                    enquiry.getPhone(),
                                    enquiry.getService(),
                                    SourceType.CONTACT,
                                    last24Hours
                            );

            if (alreadySubmitted) {
                throw new RuntimeException(
                        "You have already submitted an enquiry for this service today."
                );
            }
        }

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