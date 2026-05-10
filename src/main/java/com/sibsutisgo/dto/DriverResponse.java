package com.sibsutisgo.dto;

import com.sibsutisgo.model.CarType;
import java.time.LocalDateTime;

public record DriverResponse(
        Long id,
        String name,
        String email,
        String phone,
        String licenseNumber,
        CarType carType,
        Double rating,
        Integer ratingCount,
        boolean status,
        LocalDateTime createdAt
) {}
