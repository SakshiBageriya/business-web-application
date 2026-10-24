package com.ganesh.drycleaning.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "business_settings")
public class BusinessSettings {
    @Id
    private Long id;
    private String phone;
    private String whatsapp;
    private String email;
    @Column(length = 4000)
    private String hours;
    @Column(length = 4000)
    private String branches;
    private String mapsUrl;
    private String instagram;
    private String facebook;
    private String experience;
    private String careClaim;
    private String fabricClaim;
    private String customerClaim;

    public BusinessSettings() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String value) {
        this.phone = value;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String value) {
        this.whatsapp = value;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String value) {
        this.email = value;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String value) {
        this.hours = value;
    }

    public String getBranches() {
        return branches;
    }

    public void setBranches(String value) {
        this.branches = value;
    }

    public String getMapsUrl() {
        return mapsUrl;
    }

    public void setMapsUrl(String value) {
        this.mapsUrl = value;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String value) {
        this.instagram = value;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String value) {
        this.facebook = value;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String value) {
        this.experience = value;
    }

    public String getCareClaim() {
        return careClaim;
    }

    public void setCareClaim(String value) {
        this.careClaim = value;
    }

    public String getFabricClaim() {
        return fabricClaim;
    }

    public void setFabricClaim(String value) {
        this.fabricClaim = value;
    }

    public String getCustomerClaim() {
        return customerClaim;
    }

    public void setCustomerClaim(String value) {
        this.customerClaim = value;
    }
}
