package com.sibsutisgo.repository;

import com.sibsutisgo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByTripId(Long tripId);
    Optional<Review> findByDriverId(Long driverId);
    Optional<Review> findByPassengerId(Long passengerId);
}
