package com.sibsutisgo.repository;

import com.sibsutisgo.model.NotificationTasks;
import com.sibsutisgo.model.Trips;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationTasks, Long> {
    @Query(value = """
        UPDATE notification_tasks 
        SET status = 'PROCESSING' 
        WHERE id = (
            SELECT id FROM notification_tasks 
            WHERE status = 'PENDING' AND attempts < 3
            ORDER BY created_at ASC 
            FOR UPDATE SKIP LOCKED 
            LIMIT 1
        ) 
        RETURNING *
        """, nativeQuery = true)
    Optional<NotificationTasks> findAndLockNextTask();

    List<NotificationTasks> findByTripId(Long tripId);
}
