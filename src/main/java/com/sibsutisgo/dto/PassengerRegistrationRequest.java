package com.sibsutisgo.dto;

public record PassengerRegistrationRequest(
        String name,
        String email,
        String phone
) {}
