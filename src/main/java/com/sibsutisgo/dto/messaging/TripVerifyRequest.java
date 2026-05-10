package com.sibsutisgo.dto.messaging;

public record TripVerifyRequest(
        Long tripId,
        Long passengerId
) {}
