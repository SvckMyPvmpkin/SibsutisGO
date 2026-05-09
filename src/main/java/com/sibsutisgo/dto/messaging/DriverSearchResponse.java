package com.sibsutisgo.dto.messaging;

import com.sibsutisgo.model.CarType;
import com.sibsutisgo.model.TripsStatus;

import java.time.LocalDateTime;

public record DriverSearchResponse(
        Long driverId,
        CarType carType
) {}
