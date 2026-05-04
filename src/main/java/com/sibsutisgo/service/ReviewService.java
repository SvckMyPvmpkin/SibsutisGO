package com.sibsutisgo.service;

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

    public Review createReview(Long tripId, Integer rating, String description){
        if (rating > 5 || rating < 1) throw new IllegalArgumentException("Неверная оценка");
        Review savedReview = new Review(tripId, rating, description);
        return reviewRepository.save(savedReview);
    }

    public void deleteReview(Long id){
        reviewRepository.deleteById(id);
    }

    public Optional<Review> getReviewByTripId(Long tripId){
        return reviewRepository.findByTripId(tripId);
    }

    public Optional<Review> getReviewById(Long id){
        return reviewRepository.findById(id);
    }
}
