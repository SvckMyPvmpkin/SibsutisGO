package com.sibsutisgo.service;

import com.sibsutisgo.model.Review;
import com.sibsutisgo.model.Trips;
import com.sibsutisgo.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    public Review createReview(Trips trips, Integer rating, String description){
        //проверка на существование поездки ЧЕРЕЗ АЙДИ, но пока нет логики поездок
        if (rating > 5 || rating < 1) throw new IllegalArgumentException("Неверная оценка");
        Review review = new Review(trips, rating, description);
        return reviewRepository.save(review);
    }

    public Optional<Review> getReviewByTripId(Long tripId){
        return reviewRepository.findByTrips_Id(tripId);
    }

    public Optional<Review> getReviewById(Long id){
        return reviewRepository.findById(id);
    }
}
