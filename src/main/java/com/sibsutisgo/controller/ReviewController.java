package com.sibsutisgo.controller;

import com.sibsutisgo.dto.ReviewRequest;
import com.sibsutisgo.dto.ReviewResponse;
import com.sibsutisgo.service.ReviewService;
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
    public ResponseEntity<ReviewResponse> createReview(@RequestBody ReviewRequest reviewRequest){
        ReviewResponse savedReview = reviewService.createReview(reviewRequest.tripId(), reviewRequest.driverId(), reviewRequest.passengerId(), reviewRequest.rating(), reviewRequest.description());
        return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReviewByID(@PathVariable Long id){
        return reviewService.getReviewById(id)
                .map(review -> new ResponseEntity<>(review, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<ReviewResponse> getReviewByTripId(@PathVariable Long tripId){
        return reviewService.getReviewByTripId(tripId)
                .map(review -> new ResponseEntity<>(review, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<ReviewResponse> getReviewByDriverId(@PathVariable Long driverId){
        return reviewService.getReviewByDriverId(driverId)
                .map(review -> new ResponseEntity<>(review, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<ReviewResponse> getReviewByPassengerId(@PathVariable Long passengerId){
        return reviewService.getReviewByPassengerId(passengerId)
                .map(review -> new ResponseEntity<>(review, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
