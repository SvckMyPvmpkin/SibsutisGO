package com.sibsutisgo.dto;

public record ReviewRequest(
        Long id,
        Integer rating,
        String description
) { }
