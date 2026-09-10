package com.sndf.backend.repository;

import com.sndf.backend.model.Enquiry;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sndf.backend.model.SourceType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
	
	List<Enquiry> findBySource(SourceType source);
	
	long countByStatus(String status);
	
	Page<Enquiry> findAll(Pageable pageable);
	
	
	Page<Enquiry> findBySource(
	        SourceType source,
	        Pageable pageable
	);
	
	Page<Enquiry> findByStatus(
	        String status,
	        Pageable pageable
	);

	Page<Enquiry> findBySourceAndStatus(
	        SourceType source,
	        String status,
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
	
	
	@Query("""
			SELECT e FROM Enquiry e
			WHERE
			LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%'))
			OR e.phone LIKE CONCAT('%', :search, '%')
			OR LOWER(e.service) LIKE LOWER(CONCAT('%', :search, '%'))
			""")
			Page<Enquiry> searchAll(
			        @Param("search") String search,
			        Pageable pageable
			);
	
	@Query("""
			SELECT e FROM Enquiry e
			WHERE e.status = :status
			AND (
			LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%'))
			OR e.phone LIKE CONCAT('%', :search, '%')
			OR LOWER(e.service) LIKE LOWER(CONCAT('%', :search, '%'))
			)
			""")
			Page<Enquiry> searchByStatus(
			        @Param("status") String status,
			        @Param("search") String search,
			        Pageable pageable
			);
	
	
	@Query("""
			SELECT e FROM Enquiry e
			WHERE e.source = :source
			AND (
			LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%'))
			OR e.phone LIKE CONCAT('%', :search, '%')
			OR LOWER(e.service) LIKE LOWER(CONCAT('%', :search, '%'))
			)
			""")
			Page<Enquiry> searchBySource(
			        @Param("source") SourceType source,
			        @Param("search") String search,
			        Pageable pageable
			);
	
	
	@Query("""
			SELECT e FROM Enquiry e
			WHERE e.source = :source
			AND e.status = :status
			AND (
			LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%'))
			OR e.phone LIKE CONCAT('%', :search, '%')
			OR LOWER(e.service) LIKE LOWER(CONCAT('%', :search, '%'))
			)
			""")
			Page<Enquiry> searchBySourceAndStatus(
			        @Param("source") SourceType source,
			        @Param("status") String status,
			        @Param("search") String search,
			        Pageable pageable
			);
	
	
	
}