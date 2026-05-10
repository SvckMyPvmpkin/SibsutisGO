package com.sibsutisgo.dto.messaging;

import com.sibsutisgo.model.RecipientType;

public record SupportNotificationEvent(
        Long tripId,
        Long systemId,
        RecipientType recipientType,
        String message
) {
}
