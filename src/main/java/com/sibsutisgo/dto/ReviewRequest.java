package com.sibsutisgo.dto;

public record ReviewRequest(
        Long tripId,
        Long driverId,
        Long passengerId,
        Integer rating,
        String description
) { }
