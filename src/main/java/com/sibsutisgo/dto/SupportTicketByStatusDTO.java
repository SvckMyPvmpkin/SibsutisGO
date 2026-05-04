package com.sibsutisgo.dto;

import com.sibsutisgo.model.SupportStatus;
import com.sibsutisgo.model.SupportTicket;

import java.util.List;
import java.util.Map;

public class SupportTicketByStatusDTO {
    private final Map<SupportStatus, List<SupportTicket>> ticketsByStatus;

    public SupportTicketByStatusDTO(Map<SupportStatus, List<SupportTicket>> ticketsByStatus){
        this.ticketsByStatus = ticketsByStatus;
    }

    public Map<SupportStatus, List<SupportTicket>> getTicketsByStatus() {
        return ticketsByStatus;
    }
}
