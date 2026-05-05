package com.sibsutisgo.dto;

import com.sibsutisgo.model.SupportStatus;
import com.sibsutisgo.model.SupportTicket;

import java.util.List;
import java.util.Map;

public record SupportTicketByStatusResponse(
        Map<SupportStatus, List<SupportTicket>> ticketsByStatus
) { }
