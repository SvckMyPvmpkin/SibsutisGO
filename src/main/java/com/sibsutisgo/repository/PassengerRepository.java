package com.sibsutisgo.repository;

import com.sibsutisgo.model.Passengers;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passengers, Long> {
    Optional<Passengers> findByEmail(String email);
}
