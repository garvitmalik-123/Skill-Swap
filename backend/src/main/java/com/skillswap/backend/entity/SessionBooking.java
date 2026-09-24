package com.skillswap.backend.entity;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "session_bookings")
@CompoundIndex(name = "session_slot_idx", def = "{'sessionId': 1, 'scheduledAt': 1}", unique = true)
public class SessionBooking {

    @Id
    private String id;

    @Indexed
    private String sessionId;

    @Indexed
    private String teacherId;

    @Indexed
    private String learnerId;

    private Instant scheduledAt;

    @Builder.Default
    private BookingStatus status = BookingStatus.CONFIRMED;

    // Reference to whatever paid/covered the booking (order id or skillpoint transaction id)
    private String paymentReferenceId;

    @CreatedDate
    private Instant createdAt;

    private Instant completedAt;
    private Instant cancelledAt;

    public enum BookingStatus {
        CONFIRMED, COMPLETED, CANCELLED
    }
}