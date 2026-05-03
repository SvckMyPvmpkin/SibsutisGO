package com.sibsutisgo.service;

import com.sibsutisgo.model.*;
import com.sibsutisgo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public String registerPassenger(Passengers p) {
        if (passengerRepository.findByEmail(p.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists!");
        }
        passengerRepository.save(p);
        return jwtService.generateToken(p.getEmail());
    }

    public String registerDriver(Drivers d) {
        d.setStatus(true);
        driverRepository.save(d);
        return jwtService.generateToken(d.getEmail());
    }

    public String login(String email) {
        boolean exists = passengerRepository.findByEmail(email).isPresent() ||
                driverRepository.findByEmail(email).isPresent();
        if (!exists) throw new RuntimeException("User not found");
        return jwtService.generateToken(email);
    }

    public Passengers getPassenger(Long id) {
        return passengerRepository.findById(id).orElseThrow();
    }

    public Drivers getDriver(Long id) {
        return driverRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void updateDriverStatus(Long id, boolean status) {
        Drivers driver = driverRepository.findById(id).orElseThrow();
        driver.setStatus(status);
    }
}
