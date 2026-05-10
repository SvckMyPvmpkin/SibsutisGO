package com.sibsutisgo.mq;


import com.sibsutisgo.dto.NotificationRequest;
import com.sibsutisgo.dto.messaging.ReviewDriverRatingEvent;
import com.sibsutisgo.dto.messaging.SupportNotificationEvent;
import com.sibsutisgo.dto.messaging.TripNotificationEvent;
import com.sibsutisgo.model.RecipientType;
import com.sibsutisgo.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "notifications-queue")
public class NotificationListener {
    private final NotificationService notificationService;

    public NotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitHandler
    public void handleTripEvent(TripNotificationEvent event) {
        notificationService.createNotification(new NotificationRequest(
                event.tripId(),
                event.passengerId(),
                RecipientType.PASSENGER,
                "Trip status has changed: " + event.newStatus()
        ));

        if (event.driverId() != null) {
            notificationService.createNotification(new NotificationRequest(
                    event.tripId(), event.driverId(), RecipientType.DRIVER,
                    "Update for your order: " + event.newStatus()
            ));
        }
    }

    @RabbitHandler
    public void handleDirectNotificationRequest(NotificationRequest request) {
        notificationService.createNotification(request);
    }

    @RabbitHandler
    public void handleReviewEvent(ReviewDriverRatingEvent event) {
        notificationService.createNotification(new NotificationRequest(
                event.tripId(),
                event.passengerId(),
                RecipientType.PASSENGER,
                "Thank you for your " + event.rating().intValue() + "-star review!"
        ));

        if (event.rating() >= 5.0) {
            notificationService.createNotification(new NotificationRequest(
                    event.tripId(),
                    event.driverId(),
                    RecipientType.DRIVER,
                    "Excellent work! Passenger gave you 5 stars!"
            ));
        }
    }

    @RabbitHandler
    public void handleSupportStatusChangeEvent(SupportNotificationEvent event){
        notificationService.createNotification(new NotificationRequest(
                event.tripId(),
                event.systemId(),
                RecipientType.SYSTEM,
                event.message()
        ));
    }
}
