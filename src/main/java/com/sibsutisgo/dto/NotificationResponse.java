package com.sibsutisgo.dto;

import com.sibsutisgo.model.NotificationStatus;
import com.sibsutisgo.model.RecipientType;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        Long tripId,
        RecipientType recipientType,
        Long recipientId,
        String message,
        NotificationStatus status,
        Integer attempts,
        LocalDateTime createdAt
) {}
