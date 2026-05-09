package com.sibsutisgo.repository;

import com.sibsutisgo.model.Passengers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passengers, Long> {
    Optional<Passengers> findByEmail(String email);
}
