package com.sibsutisgo.dto.messaging;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TripCreatedEvent(
        Long tripId,
        Long passengerId,
        String origin,
        String destination,
        BigDecimal price,
        LocalDateTime createdAt
) {}
