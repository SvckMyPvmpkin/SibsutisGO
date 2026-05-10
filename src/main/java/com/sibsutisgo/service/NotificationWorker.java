package com.sibsutisgo.service;

import com.sibsutisgo.model.NotificationTasks;
import com.sibsutisgo.repository.NotificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NotificationWorker {
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;


    public NotificationWorker(NotificationRepository notificationRepository, NotificationService notificationService) {
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedDelay = 1000)
    public void fetchAndProcess() {
        Optional<NotificationTasks> task = notificationRepository.findAndLockNextTask();

        task.ifPresent(notificationService::processTasks);
    }
}
