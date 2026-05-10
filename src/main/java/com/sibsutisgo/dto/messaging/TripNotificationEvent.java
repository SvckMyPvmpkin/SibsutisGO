package com.sibsutisgo.dto.messaging;

import com.sibsutisgo.model.TripsStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TripNotificationEvent(
        @NotNull Long tripId,
        @NotNull Long passengerId,
        Long driverId,
        TripsStatus newStatus,
        LocalDateTime updatedAt
) {
}
