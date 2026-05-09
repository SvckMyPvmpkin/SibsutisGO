package com.sibsutisgo.service;

import com.sibsutisgo.dto.ReviewResponse;
import com.sibsutisgo.dto.messaging.ReviewDriverRatingEvent;
import com.sibsutisgo.model.Review;
import com.sibsutisgo.repository.ReviewRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RabbitTemplate rabbitTemplate;

    public ReviewService(ReviewRepository reviewRepository, RabbitTemplate rabbitTemplate){
        this.reviewRepository = reviewRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public ReviewResponse createReview(Long tripId, Long driverId, Long passengerId, Integer rating, String description){
        if (rating > 5 || rating < 1) throw new IllegalArgumentException("Неверная оценка");
        Review savedReview = new Review();
        savedReview.setTripId(tripId);
        savedReview.setDriverId(driverId);
        savedReview.setPassengerId(passengerId);
        savedReview.setRating(rating);
        savedReview.setDescription(description);

        Review responseReview = reviewRepository.save(savedReview);

        ReviewDriverRatingEvent reviewDriverRatingEvent = new ReviewDriverRatingEvent(
                responseReview.getTripId(),
                responseReview.getDriverId(),
                responseReview.getPassengerId(),
                responseReview.getRating(),
                responseReview.getDescription(),
                responseReview.getCreatedAt()
        );

        rabbitTemplate.convertAndSend("review-driver-rating-queue", reviewDriverRatingEvent);

        return mapToResponse(responseReview);
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
