package com.sibsutisgo.controller;


import com.sibsutisgo.dto.NotificationRequest;
import com.sibsutisgo.dto.NotificationResponse;
import com.sibsutisgo.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNotification(@Valid @RequestBody NotificationRequest request) {
        notificationService.createNotification(request);
    }

    @GetMapping
    public List<NotificationResponse> getNotificationsByTrip(@RequestParam("trip_id") Long tripId) {
        return notificationService.getNotificationsByTripId(tripId);
    }
}
