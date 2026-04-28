package com.sndf.backend.controller;

import com.sndf.backend.model.Enquiry;
import com.sndf.backend.service.EnquiryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enquiry")
public class EnquiryController {

    @Autowired
    private EnquiryService enquiryService;

    // ✅ SAVE ENQUIRY
    @PostMapping
    public ResponseEntity<Enquiry> createEnquiry(@Valid @RequestBody Enquiry enquiry) {
        Enquiry saved = enquiryService.saveEnquiry(enquiry);
        return ResponseEntity.ok(saved);
    }

    // ✅ GET ALL ENQUIRIES
    @GetMapping
    public ResponseEntity<List<Enquiry>> getAllEnquiries() {
        return ResponseEntity.ok(enquiryService.getAllEnquiries());
    }
}