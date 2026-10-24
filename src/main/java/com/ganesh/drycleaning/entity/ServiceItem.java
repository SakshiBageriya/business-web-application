package com.ganesh.drycleaning.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "service_items")
public class ServiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    private String name;
    @Column(length = 4000)
    private String description;
    @Column(precision = 10, scale = 2)
    private java.math.BigDecimal price;
    private int sortOrder;

    public ServiceItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String value) {
        this.category = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public java.math.BigDecimal getPrice() {
        return price;
    }

    public void setPrice(java.math.BigDecimal value) {
        this.price = value;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int value) {
        this.sortOrder = value;
    }
}
