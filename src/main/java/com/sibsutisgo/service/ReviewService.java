package com.sibsutisgo.service;

import com.sibsutisgo.dto.ReviewResponse;
import com.sibsutisgo.model.Review;
import com.sibsutisgo.model.Trips;
import com.sibsutisgo.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    public ReviewResponse createReview(Long tripId, Integer rating, String description){
        if (rating > 5 || rating < 1) throw new IllegalArgumentException("Неверная оценка");
        Review savedReview = new Review();
        savedReview.setTripId(tripId);
        savedReview.setRating(rating);
        savedReview.setDescription(description);

        Review responseReview = reviewRepository.save(savedReview);
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

    public ReviewResponse mapToResponse(Review review){
        return new ReviewResponse(review.getId(), review.getTripId(),
                review.getRating(), review.getDescription(), review.getCreatedAt());
    }
}
