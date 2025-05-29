package com.denniseckerskorn.repositories.user_managment_repositories;

import com.denniseckerskorn.entities.user_managment.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for managing Notification entities.
 * Provides methods to check if a notification exists by title and shipping date,
 * and to find notifications by their ID.
 */
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    boolean existsByTitleAndShippingDate(String title, LocalDateTime shippingDate);
    List<Notification> findNotificationsById(Integer id);
}
