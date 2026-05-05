package com.sibsutisgo.controller;

import com.sibsutisgo.dto.TripCreateRequest;
import com.sibsutisgo.dto.TripResponse;
import com.sibsutisgo.dto.TripStatusUpdateRequest;
import com.sibsutisgo.service.TripService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping()
    public ResponseEntity<TripResponse> createTrip(@Valid @RequestBody TripCreateRequest request) {
        TripResponse response = tripService.createTrip(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripResponse> getTripById(@PathVariable Long id) {
        return tripService.getTripById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TripResponse>> getPassengerHistory(
            @RequestParam("passenger_id") Long passengerId) {
        List<TripResponse> history = tripService.getTripsByPassengerId(passengerId);
        return ResponseEntity.ok(history);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TripResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody TripStatusUpdateRequest request) {
        TripResponse response = tripService.updateTripStatus(id, request.status());
        return ResponseEntity.ok(response);
    }
}
