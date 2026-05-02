package com.sibsutisgo.service;

import com.sibsutisgo.model.Review;
import com.sibsutisgo.model.Trips;
import com.sibsutisgo.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    //здесь подключить трипсРепозиторий

    public ReviewService(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    public Review createReview(Long trip_id, Integer rating, String description){
        //добавление поездки через АЙДИ, пока нет логики поездок закомменчено
        if (rating > 5 || rating < 1) throw new IllegalArgumentException("Неверная оценка");
        //Optional<Review> trip = tripRepository.findById(trip_id);
        //if(trip.isPresent){
        //    Trips foundTrip = trip.get();
        //    Review review = new Review(foundTrip.id, rating, description);
        //    return reviewRepository.createReview(review);
        //} else {
        //    throw new IllegalArgumentException("Нет поездки");
        // }
    }

    public void deleteReview(Long id){
        reviewRepository.deleteById(id);
    }

    public Optional<Review> getReviewByTripId(Long tripId){
        return reviewRepository.findByTrips_Id(tripId);
    }

    public Optional<Review> getReviewById(Long id){
        return reviewRepository.findById(id);
    }
}
