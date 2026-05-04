package com.sibsutisgo.repository;

import com.sibsutisgo.model.Drivers;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Drivers, Long> {
    Optional<Drivers> findByEmail(String email);
}
