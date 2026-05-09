package com.sibsutisgo.repository;

import com.sibsutisgo.model.CarType;
import com.sibsutisgo.model.Drivers;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Drivers, Long> {
    Optional<Drivers> findByEmail(String email);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Drivers> findFirstByStatusAndCarType(boolean status, CarType carType);
}
