package com.sibsutisgo.mq;


import com.sibsutisgo.dto.NotificationRequest;
import com.sibsutisgo.dto.messaging.TripNotificationEvent;
import com.sibsutisgo.model.RecipientType;
import com.sibsutisgo.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    private final NotificationService notificationService;

    public NotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "notifications-queue")
    public void handleUpdateNotification(TripNotificationEvent event) {
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
}
