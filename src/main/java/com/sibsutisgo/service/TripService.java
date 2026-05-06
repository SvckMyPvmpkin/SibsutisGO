package com.sibsutisgo.service;


import com.sibsutisgo.dto.TripCreateRequest;
import com.sibsutisgo.dto.TripResponse;
import com.sibsutisgo.dto.messaging.DriverStatusUpdateEvent;
import com.sibsutisgo.model.Trips;
import com.sibsutisgo.model.TripsStatus;
import com.sibsutisgo.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.TransactionRolledbackException;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        BigDecimal price = calculatePrice(request.origin(), request.destination());

        Trips trip = new Trips();
        trip.setPassengerId(request.passengerId());
        trip.setOrigin(request.origin());
        trip.setDestination(request.destination());
        trip.setPrice(price);

        trip.setStatus(TripsStatus.REQUESTED);

        trip.setDriverId(101L);

        Trips saved = tripRepository.save(trip);
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
                .orElseThrow(() -> new EntityNotFoundException("Поездка с ID " + id + " не найдена"));

        TripsStatus oldStatus = trip.getStatus();

        if (oldStatus == TripsStatus.CANCELLED || oldStatus == TripsStatus.COMPLETED) {
            throw new IllegalStateException("Нельзя изменять статус завершенной или отмененной поездки");
        }

        validateStatusTransition(oldStatus, newStatus);

        trip.setStatus(newStatus);

        Trips savedTrip = tripRepository.save(trip);

        if(newStatus == TripsStatus.CANCELLED || newStatus == TripsStatus.COMPLETED){
            DriverStatusUpdateEvent event = new DriverStatusUpdateEvent(trip.getDriverId(), true);
            rabbitTemplate.convertAndSend("driver-status-queue", event);
        }

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
            throw new IllegalArgumentException("Некорректный переход из статуса " + current + " в " + next);
        }
    }

    private TripResponse mapToResponse(Trips trip) {
        return new TripResponse(
                trip.getId(), trip.getPassengerId(), trip.getDriverId(),
                trip.getStatus(), trip.getOrigin(), trip.getDestination(),
                trip.getPrice(), trip.getCreatedAt()
        );
    }

    private BigDecimal calculatePrice(String origin, String destination) {
        return new BigDecimal("500.00");
    }
}
