package com.sndf.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enquiry", indexes = {
        @Index(name = "idx_source", columnList = "source"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
public class Enquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Name
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Name is required")
    private String name;

    // ✅ Phone
    @Column(nullable = false, length = 15)
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String phone;


    // ✅ Address (optional)
    @Column(length = 255)
    private String address;

    // ✅ Service
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Service is required")
    private String service;

    // ✅ Requirement
    @Column(length = 1000)
    private String requirement;

    // ✅ Source ENUM (QUICK / CONTACT)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Source is required")
    private SourceType source;
    
    @Column(nullable = false)
    private String status = "NEW";


	// ✅ Created At
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ✅ Auto set timestamp
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // ===== Getters & Setters =====

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }


    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public String getRequirement() { return requirement; }
    public void setRequirement(String requirement) { this.requirement = requirement; }

    public SourceType getSource() { return source; }
    public void setSource(SourceType source) { this.source = source; }
    
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    public LocalDateTime getCreatedAt() { return createdAt; }
}