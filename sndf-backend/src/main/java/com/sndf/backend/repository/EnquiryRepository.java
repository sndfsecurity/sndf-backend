package com.sndf.backend.repository;

import com.sndf.backend.model.Enquiry;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sndf.backend.model.SourceType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
	
	List<Enquiry> findBySource(SourceType source);
	
	long countByStatus(String status);
	
	Page<Enquiry> findAll(Pageable pageable);
	
	
	Page<Enquiry> findBySource(
	        SourceType source,
	        Pageable pageable
	);
	
	
	boolean existsByPhoneAndSourceAndCreatedAtAfter(
	        String phone,
	        SourceType source,
	        LocalDateTime dateTime
	);

	boolean existsByPhoneAndServiceAndSourceAndCreatedAtAfter(
	        String phone,
	        String service,
	        SourceType source,
	        LocalDateTime dateTime
	);
	
	
	
}