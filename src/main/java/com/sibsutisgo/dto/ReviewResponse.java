package com.sibsutisgo.dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long tripId,
        Integer rating,
        String description,
        LocalDateTime createdAt
) {
}
