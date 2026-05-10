package com.sibsutisgo.dto.messaging;

public record TripVerifyResponse(
        boolean isValid,
        String errorMessage
) {}
