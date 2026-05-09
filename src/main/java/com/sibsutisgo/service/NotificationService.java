package com.sibsutisgo.service;

import com.sibsutisgo.model.NotificationStatus;
import com.sibsutisgo.model.NotificationTasks;
import com.sibsutisgo.repository.NotificationRepository;
import com.sibsutisgo.repository.TripRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;

public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Async("notification_tasks")
    @Transactional
    public void processTasks(NotificationTasks task) {
        try {
            System.out.println(Thread.currentThread().getName()
                    + " | Sending notification ID: " + task.getId() + " for "
                    + task.getRecipientType());
            Thread.sleep(2000);

            if(Math.random() < 0.2) throw new RuntimeException("Web error");

            task.setStatus(NotificationStatus.SENT);
            System.out.println("Task " + task.getId() + " successfully sent");

        } catch(Exception e) {
            handleFailure(task);
        } finally {
            notificationRepository.save(task);
        }
    }

    private void handleFailure(NotificationTasks task) {
        int currentAttempts = task.getAttempts() + 1;

        task.setAttempts(currentAttempts);

        if(currentAttempts < 3) {
            task.setStatus(NotificationStatus.PENDING);
            System.err.println("Task error " + task.getId()
                    + ". Attempt " + currentAttempts
                    + ". Returning to queue.");
        } else {
            task.setStatus(NotificationStatus.FAILED);
            System.err.println("Task " + task.getId() + " failed");
        }
    }

}
