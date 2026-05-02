package com.sibsutisgo.repository;

import com.sibsutisgo.model.Review;
import com.sibsutisgo.model.Trips;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByTrips_Id(Long tripId);
}
