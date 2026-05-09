package com.sibsutisgo.service;

import com.sibsutisgo.dto.NotificationRequest;
import com.sibsutisgo.dto.ReviewResponse;
import com.sibsutisgo.dto.messaging.ReviewDriverRatingEvent;
import com.sibsutisgo.dto.messaging.TripVerifyRequest;
import com.sibsutisgo.dto.messaging.TripVerifyResponse;
import com.sibsutisgo.model.RecipientType;
import com.sibsutisgo.model.Review;
import com.sibsutisgo.repository.ReviewRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RabbitTemplate rabbitTemplate;

    public ReviewService(ReviewRepository reviewRepository, RabbitTemplate rabbitTemplate){
        this.reviewRepository = reviewRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public ReviewResponse createReview(Long tripId, Long driverId, Long passengerId, Integer rating, String description) {
        if (rating > 5 || rating < 1) {
            throw new IllegalArgumentException("Wrong grade: this must be from 1 to 5");
        }

        TripVerifyRequest verifyRequest = new TripVerifyRequest(tripId, passengerId);
        TripVerifyResponse verifyResponse = (TripVerifyResponse) rabbitTemplate.convertSendAndReceive(
                "trip-verify-queue",
                verifyRequest
        );

        if (verifyResponse == null || !verifyResponse.isValid()) {
            String errorMsg = (verifyResponse != null) ? verifyResponse.errorMessage() : "Trip Service timeout";
            throw new IllegalStateException("Validation failed: " + errorMsg);
        }

        Review review = new Review();
        review.setTripId(tripId);
        review.setDriverId(driverId);
        review.setPassengerId(passengerId);
        review.setRating(rating);
        review.setDescription(description);

        Review savedReview = reviewRepository.save(review);

        ReviewDriverRatingEvent event = new ReviewDriverRatingEvent(
                savedReview.getTripId(),
                savedReview.getDriverId(),
                savedReview.getPassengerId(),
                savedReview.getRating().doubleValue(),
                savedReview.getDescription(),
                savedReview.getCreatedAt()
        );

        rabbitTemplate.convertAndSend("review-driver-rating-queue", event);

        NotificationRequest passengerNotify = new NotificationRequest(
                tripId,
                passengerId,
                RecipientType.PASSENGER,
                "Спасибо за вашу оценку (" + rating + " зв.)! Нам важно ваше мнение."
        );

        rabbitTemplate.convertAndSend("notifications-queue", passengerNotify);

        if (rating == 5) {
            NotificationRequest driverNotify = new NotificationRequest(
                    tripId,
                    driverId,
                    RecipientType.DRIVER,
                    "Пассажир оценил вашу работу на 5 звезд! Так держать!"
            );
            rabbitTemplate.convertAndSend("notifications-queue", driverNotify);
        }

        return mapToResponse(savedReview);
    }

    public void deleteReview(Long id){
        reviewRepository.deleteById(id);
    }

    public Optional<ReviewResponse> getReviewByTripId(Long tripId){
        return reviewRepository.findByTripId(tripId)
                .map(this::mapToResponse);
    }

    public Optional<ReviewResponse> getReviewById(Long id){
        return reviewRepository.findById(id).map(this::mapToResponse);
    }

    public Optional<ReviewResponse> getReviewByDriverId(Long driverId){
        return reviewRepository.findByDriverId(driverId).map(this::mapToResponse);
    }

    public Optional<ReviewResponse> getReviewByPassengerId(Long passengerId){
        return reviewRepository.findByPassengerId(passengerId).map(this::mapToResponse);
    }

    public ReviewResponse mapToResponse(Review review){
        return new ReviewResponse(review.getId(), review.getTripId(), review.getDriverId(), review.getPassengerId(),
                review.getRating(), review.getDescription(), review.getCreatedAt());
    }
}
