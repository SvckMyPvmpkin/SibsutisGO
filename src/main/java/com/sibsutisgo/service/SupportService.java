package com.sibsutisgo.service;

import com.sibsutisgo.model.SupportStatus;
import com.sibsutisgo.model.SupportTicket;
import com.sibsutisgo.repository.SupportRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public List<SupportTicket> getTicketsByStatus(SupportStatus status){
        return supportRepository.findByStatus(status);
    }

    public Optional<SupportTicket> getTicketById(Long id){
        return supportRepository.findById(id);
    }
}
