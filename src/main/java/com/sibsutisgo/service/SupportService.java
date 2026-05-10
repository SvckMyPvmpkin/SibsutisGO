package com.sibsutisgo.service;

import com.sibsutisgo.dto.SupportTicketByStatusResponse;
import com.sibsutisgo.dto.SupportTicketResponse;
import com.sibsutisgo.dto.messaging.SupportNotificationEvent;
import com.sibsutisgo.model.RecipientType;
import com.sibsutisgo.model.SupportStatus;
import com.sibsutisgo.model.SupportTicket;
import com.sibsutisgo.repository.SupportRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SupportService {
    private final SupportRepository supportRepository;
    private final RabbitTemplate rabbitTemplate;

    public SupportService(SupportRepository supportRepository, RabbitTemplate rabbitTemplate) {
        this.supportRepository = supportRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public SupportTicketResponse createTicket(String message){
        SupportTicket ticket = new SupportTicket();
        ticket.setMessage(message);
        ticket.setStatus(SupportStatus.OPEN);
        SupportTicket savedTicket = supportRepository.save(ticket);
        SupportNotificationEvent event = new SupportNotificationEvent(
                null,
                0L,
                RecipientType.SYSTEM,
                "Ticket #" + ticket.getId() + " has been created!"
        );
        rabbitTemplate.convertAndSend("notifications-queue", event);
        return mapToResponse(savedTicket);
    }

    public SupportTicketResponse changeStatusTicket(Long ticketId, SupportStatus status){
        SupportTicket foundTicket = supportRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("No such ticket"));

        if (foundTicket.getStatus() == status) {
            return mapToResponse(foundTicket);
        }

        foundTicket.setStatus(status);
        if(status == SupportStatus.CLOSED ) {
            foundTicket.setClosedAt(LocalDateTime.now());
        } else {
            foundTicket.setClosedAt(null);
        }

        SupportTicket savedTicket = supportRepository.save(foundTicket);
        String notificationMessage;
        if (status == SupportStatus.CLOSED) {
            notificationMessage = "Ticket #" + savedTicket.getId() + " has been closed";
        } else {
            notificationMessage = "Ticket #" + savedTicket.getId() + " status changed to " + status;
        }

        SupportNotificationEvent event = new SupportNotificationEvent(
                null,
                0L,
                RecipientType.SYSTEM,
                notificationMessage
        );
        rabbitTemplate.convertAndSend("notifications-queue", event);
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
