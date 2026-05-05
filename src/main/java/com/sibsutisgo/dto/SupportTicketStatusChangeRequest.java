package com.sibsutisgo.dto;

import com.sibsutisgo.model.SupportStatus;

public record SupportTicketStatusChangeRequest(
        SupportStatus status
) {
}
