package com.sibsutisgo.service;

import com.sibsutisgo.dto.SupportTicketByStatusDTO;
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

    public SupportTicket createTicket(String message){
        SupportTicket ticket = new SupportTicket(message);
        return supportRepository.save(ticket);
    }

    public SupportTicket closeTicket(Long ticketId){
        SupportTicket foundTicket = supportRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Нет такого тикета"));

        if (foundTicket.getStatus() == SupportStatus.CLOSED) {
            return foundTicket;
        }

        foundTicket.setStatus(SupportStatus.CLOSED);
        foundTicket.setClosedAt(LocalDateTime.now());

        return supportRepository.save(foundTicket);
    }

    private SupportTicketByStatusDTO getTicketsByStatuses(List<SupportStatus> statuses) {
        Map<SupportStatus, List<SupportTicket>> map = new EnumMap<>(SupportStatus.class);
        for (SupportStatus status : statuses) {
            map.put(status, supportRepository.findByStatus(status));
        }
        return new SupportTicketByStatusDTO(map);
    }

    public SupportTicketByStatusDTO getActiveTickets() {
        return getTicketsByStatuses(List.of(SupportStatus.OPEN, SupportStatus.IN_PROGRESS));
    }

    public SupportTicketByStatusDTO getClosedTickets() {
        return getTicketsByStatuses(List.of(SupportStatus.CLOSED));
    }

    public Optional<SupportTicket> getTicketById(Long id){
        return supportRepository.findById(id);
    }
}
