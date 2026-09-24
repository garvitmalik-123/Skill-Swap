package com.skillswap.backend.repository;

import com.skillswap.backend.entity.SessionBooking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SessionBookingRepository extends MongoRepository<SessionBooking, String> {

    List<SessionBooking> findByLearnerId(String learnerId);

    List<SessionBooking> findByTeacherId(String teacherId);

    Optional<SessionBooking> findBySessionIdAndScheduledAtAndStatus(
            String sessionId, Instant scheduledAt, SessionBooking.BookingStatus status);
}