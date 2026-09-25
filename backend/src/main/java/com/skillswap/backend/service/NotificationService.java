package com.skillswap.backend.service;

import com.skillswap.backend.dto.response.NotificationResponse;
import com.skillswap.backend.entity.Notification.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    void notify(String userId, NotificationType type, String title, String message,
                String referenceId, String referenceType);

    Page<NotificationResponse> getMyNotifications(String userId, Pageable pageable);

    void markAsRead(String notificationId, String userId);

    void markAllAsRead(String userId);

    long getUnreadCount(String userId);
}