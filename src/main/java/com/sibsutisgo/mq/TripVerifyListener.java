package com.sibsutisgo.mq;

import com.sibsutisgo.dto.messaging.TripVerifyRequest;
import com.sibsutisgo.dto.messaging.TripVerifyResponse;
import com.sibsutisgo.model.TripsStatus;
import com.sibsutisgo.repository.TripRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TripVerifyListener {
    private final TripRepository tripRepository;

    public TripVerifyListener(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @RabbitListener(queues = "trip-verify-queue")
    public TripVerifyResponse handleVerifyRequest(TripVerifyRequest request) {
        return tripRepository.findById(request.tripId())
                .map(trip -> {
                    if (!trip.getPassengerId().equals(request.passengerId())) {
                        return new TripVerifyResponse(false, "Trip belongs to another passenger");
                    }

                    if (trip.getStatus() != TripsStatus.COMPLETED) {
                        return new TripVerifyResponse(false, "Cannot review non-completed trip");
                    }
                    return new TripVerifyResponse(true, null);
                })
                .orElse(new TripVerifyResponse(false, "Trip not found"));
    }
}