package com.sndf.backend.controller;

import com.sndf.backend.model.Enquiry;
import com.sndf.backend.service.EnquiryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enquiry")
public class EnquiryController {

    @Autowired
    private EnquiryService enquiryService;
    
    private final Map<String, Long> requestTracker = new HashMap<>();
    

    // ✅ SAVE ENQUIRY
    @PostMapping
    public ResponseEntity<Enquiry> createEnquiry(@Valid @RequestBody Enquiry enquiry) {
        Enquiry saved = enquiryService.saveEnquiry(enquiry);
        return ResponseEntity.ok(saved);
    }

    // ✅ GET ALL ENQUIRIES
    @GetMapping
    public ResponseEntity<List<Enquiry>> getEnquiries(
            @RequestParam(required = false) String source
    ) {
        return ResponseEntity.ok(enquiryService.getEnquiriesBySource(source));
    }
    
    
//    @PutMapping("/{id}/status")
//    public ResponseEntity<Enquiry> updateStatus(
//            @PathVariable Long id,
//            @RequestParam String status) {
//
//        Enquiry updated = enquiryService.updateStatus(id, status);
//        return ResponseEntity.ok(updated);
//    }
    
    
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        try {
            Enquiry updated = enquiryService.updateStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnquiry(@PathVariable Long id) {
        enquiryService.deleteEnquiry(id);
        return ResponseEntity.ok("Enquiry deleted successfully");
    }
}