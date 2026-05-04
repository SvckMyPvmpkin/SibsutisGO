package com.sibsutisgo.dto;

public record DriverRegistrationRequest(
        String name,
        String email,
        String phone,
        String licenseNumber
) {}
