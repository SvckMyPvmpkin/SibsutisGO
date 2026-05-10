package com.sibsutisgo.dto.messaging;

import java.time.LocalDateTime;

public record ReviewDriverRatingEvent(
        Long tripId,
        Long driverId,
        Long passengerId,
        Double rating,
        String description,
        LocalDateTime createdAt
) {}
