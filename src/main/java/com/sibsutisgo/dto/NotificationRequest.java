package com.sibsutisgo.dto;

import com.sibsutisgo.model.RecipientType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationRequest(
        @NotNull Long tripId,
        @NotNull Long recipientId,
        @NotNull RecipientType recipientType,
        @NotBlank String message
) {}
