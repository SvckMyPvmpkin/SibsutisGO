package com.sibsutisgo.service;


import com.sibsutisgo.dto.TripCreateRequest;
import com.sibsutisgo.dto.TripResponse;
import com.sibsutisgo.dto.messaging.*;
import com.sibsutisgo.model.Trips;
import com.sibsutisgo.model.TripsStatus;
import com.sibsutisgo.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TripService {
    private final TripRepository tripRepository;
    private final RabbitTemplate rabbitTemplate;

    public TripService(TripRepository tripRepository, RabbitTemplate rabbitTemplate) {
        this.tripRepository = tripRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public TripResponse createTrip(TripCreateRequest request) {
        RouteRequest routeRequest = new RouteRequest(request.origin(), request.destination());
        RouteResponse routeResponse = (RouteResponse) rabbitTemplate.convertSendAndReceive(
                "route-request-queue",
                routeRequest
        );

        if (routeResponse == null) {
            throw new RuntimeException("Routing service is unavailable");
        }

        double km = routeResponse.distanceKm();
        double carMultiplier = request.carType().getCoefficient();
        double trafficMultiplier = getTrafficCongestion();

        BigDecimal price = BigDecimal.valueOf(km)
                .multiply(BigDecimal.valueOf(40.0))         // расстояние * 40
                .multiply(BigDecimal.valueOf(carMultiplier)) // * коэффициент машины
                .multiply(BigDecimal.valueOf(trafficMultiplier)) // * пробки
                .setScale(2, RoundingMode.HALF_UP);         // округление до копеек



        PassengerVerifyRequest verifyRequest = new PassengerVerifyRequest(request.passengerId());

        PassengerVerifyResponse verifyResponse = (PassengerVerifyResponse) rabbitTemplate.convertSendAndReceive(
                "passenger-verify-queue",
                verifyRequest
        );

        if (verifyResponse == null || !verifyResponse.isExist()) {
            throw new RuntimeException("Passenger doesn't exist");
        }

        DriverSearchRequest searchRequest = new DriverSearchRequest(true, request.carType());

        DriverSearchResponse searchResponse = (DriverSearchResponse) rabbitTemplate.convertSendAndReceive(
                "driver-search-queue",
                searchRequest
        );

        if (searchResponse == null || searchResponse.driverId() == null || searchResponse.carType() != searchRequest.carType()) {
            throw new RuntimeException("No free drivers");
        }

        Trips trip = new Trips();
        trip.setPassengerId(request.passengerId());
        trip.setOrigin(request.origin());
        trip.setDestination(request.destination());
        trip.setPrice(price);
        trip.setDriverId(searchResponse.driverId());

        trip.setStatus(TripsStatus.REQUESTED);

        Trips saved = tripRepository.save(trip);

        TripNotificationEvent notificationEvent = new TripNotificationEvent(
                saved.getId(),
                saved.getPassengerId(),
                saved.getDriverId(),
                saved.getStatus(),
                LocalDateTime.now()
        );

        rabbitTemplate.convertAndSend("notifications-queue", notificationEvent);

        return mapToResponse(saved);
    }

    public Optional<TripResponse> getTripById(Long id) {
        return tripRepository.findById(id)
                .map(this::mapToResponse);
    }

    public List<TripResponse> getTripsByPassengerId(Long passengerId) {
        return tripRepository.findByPassengerId(passengerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public TripResponse updateTripStatus(Long id, TripsStatus newStatus) {
        Trips trip = tripRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trip with ID " + id + " doesn't found"));

        TripsStatus oldStatus = trip.getStatus();

        if (oldStatus == TripsStatus.CANCELLED || oldStatus == TripsStatus.COMPLETED) {
            throw new IllegalStateException("You can't change CANCELLED or COMPLETED trip status");
        }

        validateStatusTransition(oldStatus, newStatus);

        trip.setStatus(newStatus);

        Trips savedTrip = tripRepository.save(trip);

        if(newStatus == TripsStatus.CANCELLED || newStatus == TripsStatus.COMPLETED){
            DriverStatusUpdateEvent event = new DriverStatusUpdateEvent(trip.getDriverId(), true);
            rabbitTemplate.convertAndSend("driver-status-queue", event);
        }

        TripNotificationEvent notificationEvent = new TripNotificationEvent(
                savedTrip.getId(),
                savedTrip.getPassengerId(),
                savedTrip.getDriverId(),
                savedTrip.getStatus(),
                LocalDateTime.now()
        );

        rabbitTemplate.convertAndSend("notifications-queue", notificationEvent);

        return new TripResponse(savedTrip.getId(),
                savedTrip.getPassengerId(), savedTrip.getDriverId(),
                savedTrip.getStatus(), savedTrip.getOrigin(), savedTrip.getDestination(), savedTrip.getPrice(), savedTrip.getCreatedAt());
    }

    private void validateStatusTransition(TripsStatus current, TripsStatus next) {
        boolean isValid = switch (current) {
            case REQUESTED -> next == TripsStatus.ACCEPTED || next == TripsStatus.CANCELLED;
            case ACCEPTED -> next == TripsStatus.IN_PROGRESS || next == TripsStatus.CANCELLED;
            case IN_PROGRESS -> next == TripsStatus.COMPLETED;
            default -> false;
        };

        if (!isValid) {
            throw new IllegalArgumentException("Incorrect transition from status " + current + " to " + next);
        }
    }

    private TripResponse mapToResponse(Trips trip) {
        return new TripResponse(
                trip.getId(), trip.getPassengerId(), trip.getDriverId(),
                trip.getStatus(), trip.getOrigin(), trip.getDestination(),
                trip.getPrice(), trip.getCreatedAt()
        );
    }

    private double getTrafficCongestion() {
        int hour = java.time.LocalTime.now().getHour();

        if (hour >= 17 && hour < 19) return 1.9;

        if (hour >= 8 && hour < 11) return 1.7;

        if (hour >= 11 && hour < 16) return 1.45;

        if (hour == 7 || hour == 16 || (hour >= 19 && hour < 21)) return 1.3;

        if (hour >= 21 && hour < 23) return 1.15;

        return 1.0;
    }
}
