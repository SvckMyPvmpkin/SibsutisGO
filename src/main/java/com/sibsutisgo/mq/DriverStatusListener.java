package com.sibsutisgo.mq;

import com.sibsutisgo.dto.messaging.DriverStatusUpdateEvent;
import com.sibsutisgo.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DriverStatusListener {

    private final UserService userService;

    public DriverStatusListener(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = "driver-status-queue")
    public void handleDriverStatusUpdate(DriverStatusUpdateEvent event) {
        userService.updateDriverStatus(event.driverId(), event.driverStatus());
    }
}