package com.sndf.backend.controller;

import com.sndf.backend.model.Enquiry;
import com.sndf.backend.service.EnquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/enquiry")

public class EnquiryController {

    @Autowired
    private EnquiryService enquiryService;

    // ✅ SAVE ENQUIRY (POST API)
    @PostMapping
    public Enquiry createEnquiry(@RequestBody Enquiry enquiry) {
        return enquiryService.saveEnquiry(enquiry);
    }

    // ✅ GET ALL ENQUIRIES (for future admin)
    @GetMapping
    public List<Enquiry> getAllEnquiries() {
        return enquiryService.getAllEnquiries();
    }
}