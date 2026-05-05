package com.sndf.backend.repository;

import com.sndf.backend.model.Enquiry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sndf.backend.model.SourceType;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
	
	List<Enquiry> findBySource(SourceType source);
}