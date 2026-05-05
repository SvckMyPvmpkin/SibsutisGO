package com.sibsutisgo.service;

import com.sibsutisgo.dto.SupportTicketByStatusResponse;
import com.sibsutisgo.dto.SupportTicketResponse;
import com.sibsutisgo.model.SupportStatus;
import com.sibsutisgo.model.SupportTicket;
import com.sibsutisgo.repository.SupportRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SupportService {
    private final SupportRepository supportRepository;

    public SupportService(SupportRepository supportRepository) {
        this.supportRepository = supportRepository;
    }

    public SupportTicketResponse createTicket(String message){
        SupportTicket ticket = new SupportTicket();
        ticket.setMessage(message);
        ticket.setStatus(SupportStatus.OPEN);
        SupportTicket savedTicket = supportRepository.save(ticket);
        return mapToResponse(savedTicket);
    }

    public SupportTicketResponse changeStatusTicket(Long ticketId, SupportStatus status){
        SupportTicket foundTicket = supportRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Нет такого тикета"));

        if (foundTicket.getStatus() == status) {
            return mapToResponse(foundTicket);
        }

        foundTicket.setStatus(status);
        if(status == SupportStatus.CLOSED ) foundTicket.setClosedAt(LocalDateTime.now());

        SupportTicket savedTicket = supportRepository.save(foundTicket);
        return mapToResponse(savedTicket);
    }

    private SupportTicketByStatusResponse getTicketsByStatuses(List<SupportStatus> statuses) {
        Map<SupportStatus, List<SupportTicket>> map = new EnumMap<>(SupportStatus.class);
        for (SupportStatus status : statuses) {
            map.put(status, supportRepository.findByStatus(status));
        }
        return new SupportTicketByStatusResponse(map);
    }

    public SupportTicketByStatusResponse getActiveTickets() {
        return getTicketsByStatuses(List.of(SupportStatus.OPEN, SupportStatus.IN_PROGRESS));
    }

    public SupportTicketByStatusResponse getClosedTickets() {
        return getTicketsByStatuses(List.of(SupportStatus.CLOSED));
    }

    public Optional<SupportTicketResponse> getTicketById(Long id){
        return supportRepository.findById(id).map(this::mapToResponse);
    }

    public SupportTicketResponse mapToResponse(SupportTicket ticket){
        return new SupportTicketResponse(ticket.getId(), ticket.getMessage(),
                ticket.getStatus(), ticket.getCreatedAt(), ticket.getClosedAt());
    }
}
