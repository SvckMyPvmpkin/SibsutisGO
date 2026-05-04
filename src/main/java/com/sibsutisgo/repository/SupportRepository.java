package com.sibsutisgo.repository;

import com.sibsutisgo.model.SupportStatus;
import com.sibsutisgo.model.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByStatus(SupportStatus status);
}
