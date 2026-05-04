package com.sibsutisgo.dto;

import com.sibsutisgo.model.TripsStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TripResponse(
        Long id,
        Long passengerId,
        Long driverId,
        TripsStatus status,
        String origin,
        String destination,
        BigDecimal price,
        LocalDateTime created_at
) {}
