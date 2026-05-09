package com.sibsutisgo.service;

import com.sibsutisgo.dto.*;
import com.sibsutisgo.model.*;
import com.sibsutisgo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final PassengerRepository passengerRepository;
    private final DriverRepository driverRepository;
    private final JwtService jwtService;

    public UserService(
            PassengerRepository passengerRepository,
            DriverRepository driverRepository,
            JwtService jwtService) {
            this.passengerRepository = passengerRepository;
            this.driverRepository = driverRepository;
            this.jwtService = jwtService;
        }

    public PassengerResponse registerPassenger(PassengerRegistrationRequest dto) {
        if (passengerRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        Passengers passenger = new Passengers();
        passenger.setName(dto.name());
        passenger.setEmail(dto.email());
        passenger.setPhone(dto.phone());
        passenger.setCreatedAt(LocalDateTime.now());

        Passengers savedPassenger = passengerRepository.save(passenger);

        return new PassengerResponse(
                savedPassenger.getId(),
                savedPassenger.getName(),
                savedPassenger.getEmail(),
                savedPassenger.getPhone(),
                savedPassenger.getCreatedAt()
        );
    }

    public AuthResponse login(String email) {
        String userEmail = passengerRepository.findByEmail(email)
                .map(Passengers::getEmail)
                .orElseGet(() -> driverRepository.findByEmail(email)
                        .map(Drivers::getEmail)
                        .orElseThrow(() -> new RuntimeException("User not found with email: " + email))
                );

        String token = jwtService.generateToken(userEmail);

        return new AuthResponse(token);
    }

    public DriverResponse registerDriver(DriverRegistrationRequest dto) {
        if (driverRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Driver with this email already exists");
        }

        Drivers driver = new Drivers();
        driver.setName(dto.name());
        driver.setEmail(dto.email());
        driver.setPhone(dto.phone());
        driver.setLicenseNumber(dto.licenseNumber());
        driver.setCarType(dto.carType());
        driver.setStatus(true);
        driver.setRating(0.0);
        driver.setRatingCount(0);
        driver.setCreatedAt(LocalDateTime.now());

        Drivers saved = driverRepository.save(driver);

        return new DriverResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getPhone(),
                saved.getLicenseNumber(),
                saved.getCarType(),
                saved.getRating(),
                saved.getRatingCount(),
                saved.isStatus(),
                saved.getCreatedAt()
        );
    }

    public DriverResponse getDriverById(Long id) {
        Drivers driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        return new DriverResponse(
                driver.getId(),
                driver.getName(),
                driver.getEmail(),
                driver.getPhone(),
                driver.getLicenseNumber(),
                driver.getCarType(),
                driver.getRating(),
                driver.getRatingCount(),
                driver.isStatus(),
                driver.getCreatedAt()
        );
    }

    public Passengers getPassenger(Long id) {
        return passengerRepository.findById(id).orElseThrow();
    }

    public boolean isPassengerExist(Long id){
        return passengerRepository.existsById(id);
    }

    public Drivers getDriver(Long id) {
        return driverRepository.findById(id).orElseThrow();
    }

    public void updateDriverStatus(Long id, boolean newStatus) {
        Drivers driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + id));

        driver.setStatus(newStatus);
        driverRepository.save(driver);
    }

    @Transactional
    public Long getAvailableDriverId(CarType carType) {
        return driverRepository.findFirstByStatusAndCarType(true, carType)
                .map(driver -> {
                    driver.setStatus(false);
                    driverRepository.save(driver);
                    return driver.getId();
                })
                .orElse(null);
    }

    public void updateRating(Long driverId, Double newGrade) {
        Drivers driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        if (newGrade == null) {
            throw new IllegalArgumentException("Grade can't be null");
        }

        double currentRating = (driver.getRating() != null) ? driver.getRating().doubleValue() : 0.0;
        int count = (driver.getRatingCount() != null) ? driver.getRatingCount() : 0;

        if (count == 0) {
            driver.setRating(newGrade);
            driver.setRatingCount(1);
        } else {
            double updatedRating = ((currentRating * count) + newGrade) / (count + 1);

            updatedRating = Math.round(updatedRating * 100.0) / 100.0;

            driver.setRating(updatedRating);
            driver.setRatingCount(count + 1);
        }

        driverRepository.save(driver);
    }
}
