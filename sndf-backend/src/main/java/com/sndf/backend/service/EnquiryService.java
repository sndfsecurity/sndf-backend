package com.sndf.backend.service;

import com.sndf.backend.model.Enquiry;
import com.sndf.backend.repository.EnquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sndf.backend.model.SourceType;

import java.time.LocalDate;
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

            String source,

            String status,

            String search,

            LocalDate date

    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt").descending()
                );

        boolean hasSearch =
                search != null &&
                !search.trim().isEmpty();

        search = hasSearch ? search.trim() : null;


        // DATE FILTER
        // If date is selected, handle date + source + status + search together
        if (date != null) {

            LocalDateTime startDate =
                    date.atStartOfDay();

            LocalDateTime endDate =
                    date.plusDays(1).atStartOfDay();

            SourceType sourceEnum = null;

            if (source != null && !source.isEmpty()) {
                sourceEnum =
                        SourceType.valueOf(source.toUpperCase());
            }

            String statusValue = null;

            if (status != null && !status.isEmpty()) {
                statusValue = status.toUpperCase();
            }

            return enquiryRepository.searchByDateWithFilters(
                    startDate,
                    endDate,
                    sourceEnum,
                    statusValue,
                    search,
                    pageable
            );
        }


        // EXISTING SEARCH + SOURCE + STATUS FILTER
        if (hasSearch
                && source != null && !source.isEmpty()
                && status != null && !status.isEmpty()) {

            SourceType sourceEnum =
                    SourceType.valueOf(source.toUpperCase());

            return enquiryRepository.searchBySourceAndStatus(
                    sourceEnum,
                    status.toUpperCase(),
                    search,
                    pageable
            );
        }

        if (hasSearch
                && source != null && !source.isEmpty()) {

            SourceType sourceEnum =
                    SourceType.valueOf(source.toUpperCase());

            return enquiryRepository.searchBySource(
                    sourceEnum,
                    search,
                    pageable
            );
        }

        if (hasSearch
                && status != null && !status.isEmpty()) {

            return enquiryRepository.searchByStatus(
                    status.toUpperCase(),
                    search,
                    pageable
            );
        }

        if (hasSearch) {

            return enquiryRepository.searchAll(
                    search,
                    pageable
            );
        }


        // SOURCE + STATUS FILTER
        if (source != null && !source.isEmpty()
                && status != null && !status.isEmpty()) {

            SourceType sourceEnum =
                    SourceType.valueOf(source.toUpperCase());

            return enquiryRepository.findBySourceAndStatus(
                    sourceEnum,
                    status.toUpperCase(),
                    pageable
            );
        }


        // SOURCE ONLY
        if (source != null && !source.isEmpty()) {

            SourceType sourceEnum =
                    SourceType.valueOf(source.toUpperCase());

            return enquiryRepository.findBySource(
                    sourceEnum,
                    pageable
            );
        }


        // STATUS ONLY
        if (status != null && !status.isEmpty()) {

            return enquiryRepository.findByStatus(
                    status.toUpperCase(),
                    pageable
            );
        }


        // ALL
        return enquiryRepository.findAll(pageable);
    }

    
    
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