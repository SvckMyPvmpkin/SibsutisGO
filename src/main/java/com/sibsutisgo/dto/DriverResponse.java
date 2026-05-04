package com.sibsutisgo.dto;

import java.time.LocalDateTime;

public record DriverResponse(
        Long id,
        String name,
        String email,
        String phone,
        String licenseNumber,
        boolean status,
        LocalDateTime createdAt
) {}
