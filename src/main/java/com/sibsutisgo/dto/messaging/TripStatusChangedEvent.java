package com.sibsutisgo.dto.messaging;

import com.sibsutisgo.model.TripsStatus;

import java.time.LocalDateTime;

public record TripStatusChangedEvent(
        Long tripId,
        Long passengerId,
        Long driverId,
        TripsStatus oldStatus,
        TripsStatus newStatus,
        LocalDateTime updatedAt
) {}
