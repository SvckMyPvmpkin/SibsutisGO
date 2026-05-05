package com.sibsutisgo.dto;

import com.sibsutisgo.model.SupportStatus;

import java.time.LocalDateTime;

public record SupportTicketResponse(
        Long id,
        String message,
        SupportStatus status,
        LocalDateTime createdAt,
        LocalDateTime closedAt
) { }
