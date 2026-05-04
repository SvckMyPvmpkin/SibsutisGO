package com.sibsutisgo.dto;

public record PassengerResponse(
        Long id,
        String name,
        String email,
        String phone,
        java.time.LocalDateTime createdAt
) {}
