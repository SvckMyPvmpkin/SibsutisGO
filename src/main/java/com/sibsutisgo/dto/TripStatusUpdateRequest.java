package com.sibsutisgo.dto;

import com.sibsutisgo.model.TripsStatus;

public record TripStatusUpdateRequest(
        TripsStatus status
) {}
