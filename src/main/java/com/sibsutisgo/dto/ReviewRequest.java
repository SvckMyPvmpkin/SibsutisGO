package com.sibsutisgo.dto;

public record ReviewRequest(
        Long tripId,
        Integer rating,
        String description
) { }
