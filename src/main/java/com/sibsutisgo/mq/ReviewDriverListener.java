package com.sibsutisgo.mq;


import com.sibsutisgo.dto.messaging.ReviewDriverRatingEvent;
import com.sibsutisgo.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReviewDriverListener {
    private final UserService userService;

    public ReviewDriverListener(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = "review-driver-rating-queue")
    public void handleDriverRating(ReviewDriverRatingEvent event){
        userService.updateRating(event.driverId(), event.rating());
    }
}
