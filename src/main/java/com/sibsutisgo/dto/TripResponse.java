package com.sibsutisgo.dto;

import com.sibsutisgo.model.TripsStatus;

import java.io.Serial;
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
) implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
