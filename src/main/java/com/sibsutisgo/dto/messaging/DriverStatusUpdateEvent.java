package com.sibsutisgo.dto.messaging;

public record DriverStatusUpdateEvent(
        Long driverId,
        boolean driverStatus
) {
}
