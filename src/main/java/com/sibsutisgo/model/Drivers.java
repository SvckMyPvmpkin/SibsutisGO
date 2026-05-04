package com.sibsutisgo.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="drivers")

public class Drivers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "phone", nullable = false, length = 12)
    private String phone;

    @Column(name = "license_number", nullable = false, length = 16)
    private String license_number;

    // Статус доступности водителя для выдачи заказа
    // 0 - недоступен
    // 1 - доступен
    @Column(name = "status", nullable = false)
    private boolean status;

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false,  updatable = false)
    private LocalDateTime createdAt;

    public Drivers(){
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

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getLicenseNumber() {
        return license_number;
    }

    public void setLicenseNumber(String s) {
        this.license_number = s;
    }
}
