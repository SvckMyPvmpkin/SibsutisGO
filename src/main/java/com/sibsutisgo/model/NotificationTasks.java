package com.sibsutisgo.model;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "notification_tasks")

public class NotificationTasks {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "trip_id")
        private Long tripId;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private RecipientType recipientType;

        @Column(name = "recipient_id", nullable = false)
        private Long recipientId;

        @Column(nullable = false, columnDefinition = "TEXT")
        private String message;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private NotificationStatus status;

        @Column(nullable = false)
        private Integer attempts = 0;

        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        public NotificationTasks() {}

        public Long getId() {
            return id;
        }

        public Long getTripId() {
            return tripId;
        }

        public void setTripId(Long tripId) {
            this.tripId = tripId;
        }

        public RecipientType getRecipientType() {
            return recipientType;
        }
        public void setRecipientType(RecipientType recipientType) {
            this.recipientType = recipientType;
        }

        public Long getRecipientId() {
            return recipientId;
        }
        public void setRecipientId(Long recipientId) {
            this.recipientId = recipientId;
        }

        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }

        public NotificationStatus getStatus() {
            return status;
        }
        public void setStatus(NotificationStatus status) {
            this.status = status;
        }

        public Integer getAttempts() {
            return attempts;
        }
        public void setAttempts(Integer attempts) {
            this.attempts = attempts;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
}
