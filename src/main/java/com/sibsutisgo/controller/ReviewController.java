package com.sibsutisgo.controller;

import com.sibsutisgo.dto.ReviewRequestDTO;
import com.sibsutisgo.model.Review;
import com.sibsutisgo.model.Trips;
import com.sibsutisgo.service.ReviewService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequestDTO reviewRequest){
        Review savedReview = reviewService.createReview(reviewRequest.getId(), reviewRequest.getRating(), reviewRequest.getDescription());
        return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewByID(@PathVariable Long id){
        return reviewService.getReviewById(id)
                .map(review -> new ResponseEntity<>(review, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/trip/{id}")
    public ResponseEntity<Review> getReviewByTripId(@PathVariable Long trip_id){
        return reviewService.getReviewByTripId(trip_id)
                .map(review -> new ResponseEntity<>(review, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
