package com.sibsutisgo.controller;

import com.sibsutisgo.dto.*;
import com.sibsutisgo.model.Passengers;
import com.sibsutisgo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/passengers")
    public ResponseEntity<PassengerResponse> regPassenger(@RequestBody PassengerRegistrationRequest regRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerPassenger(regRequest));
    }

    @PostMapping("/drivers")
    public ResponseEntity<DriverResponse> regDriver(@RequestBody DriverRegistrationRequest regRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerDriver(regRequest));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest.email()));
    }

    @GetMapping("/passengers/{id}")
    public ResponseEntity<Passengers> getP(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getPassenger(id));
    }

    @GetMapping("/drivers/{id}")
    public ResponseEntity<DriverResponse> getDriver(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getDriverById(id));
    }

    @PatchMapping("/drivers/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody DriverStatusRequest request) {

        userService.updateDriverStatus(id, request.newStatus());
        return ResponseEntity.noContent().build();
    }
}
