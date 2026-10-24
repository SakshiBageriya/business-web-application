package com.ganesh.drycleaning.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pickup_requests")
public class PickupRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String email;
    @Column(length = 4000)
    private String address;
    private String service;
    private java.time.LocalDate preferredDate;
    private String preferredTime;
    @Column(length = 4000)
    private String notes;
    @Enumerated(EnumType.STRING)
    private PickupStatus status;
    private java.time.Instant createdAt;

    public PickupRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String value) {
        this.phone = value;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String value) {
        this.email = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String value) {
        this.address = value;
    }

    public String getService() {
        return service;
    }

    public void setService(String value) {
        this.service = value;
    }

    public java.time.LocalDate getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(java.time.LocalDate value) {
        this.preferredDate = value;
    }

    public String getPreferredTime() {
        return preferredTime;
    }

    public void setPreferredTime(String value) {
        this.preferredTime = value;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String value) {
        this.notes = value;
    }

    public PickupStatus getStatus() {
        return status;
    }

    public void setStatus(PickupStatus value) {
        this.status = value;
    }

    public java.time.Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.Instant value) {
        this.createdAt = value;
    }
}
