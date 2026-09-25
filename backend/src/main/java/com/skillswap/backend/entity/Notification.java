package com.skillswap.backend.entity;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    @Indexed
    private String userId; // recipient

    private NotificationType type;

    private String title;
    private String message;

    // Optional link back to the relevant entity (courseId, requestId, bookingId, etc.)
    private String referenceId;
    private String referenceType; // e.g. "COURSE", "SKILL_EXCHANGE_REQUEST", "SESSION_BOOKING"

    @Builder.Default
    private boolean read = false;

    @CreatedDate
    private Instant createdAt;

    public enum NotificationType {
        COURSE_ENROLLMENT,
        NEW_LEARNER_ENROLLED,
        COURSE_COMPLETION,
        NEW_REVIEW,
        SKILLPOINTS_EARNED,
        SKILLPOINTS_SPENT,
        PAYMENT_RECEIVED,
        SKILL_EXCHANGE_REQUEST,
        EXCHANGE_RESPONSE,
        SESSION_BOOKING,
        SESSION_COMPLETED,
        ADMIN_ACTION
    }
}