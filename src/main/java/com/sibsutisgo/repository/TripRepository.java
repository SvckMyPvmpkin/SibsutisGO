package com.sibsutisgo.repository;

import com.sibsutisgo.model.Trips;
import com.sibsutisgo.model.TripsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trips, Long> {
    List<Trips> findByPassengerId(Long passengerId);

    List<Trips> findByStatus(TripsStatus status);

    List<Trips> findByPassengerIdOrderByCreatedAtDesc(Long passengerId);

    long countByCreatedAtAfter(java.time.LocalDateTime dateTime);

    @Query("SELECT AVG(t.price) FROM Trips t WHERE t.createdAt >= :dateTime")
    BigDecimal getAveragePriceAfter(java.time.LocalDateTime dateTime);
}
