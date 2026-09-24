package com.skillswap.backend.dto.response;

import com.skillswap.backend.entity.SessionBooking.BookingStatus;
import lombok.*;

import java.time.Instant;

@Data
@Builder
public class BookingResponse {
    private String id;
    private String sessionId;
    private String sessionTitle;
    private String teacherId;
    private String teacherName;
    private String learnerId;
    private String learnerName;
    private Instant scheduledAt;
    private BookingStatus status;
    private Instant createdAt;
    private Instant completedAt;
}