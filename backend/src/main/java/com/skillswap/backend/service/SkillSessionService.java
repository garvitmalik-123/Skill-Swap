package com.skillswap.backend.service;

import com.skillswap.backend.dto.request.SessionBookRequest;
import com.skillswap.backend.dto.request.SessionCreateRequest;
import com.skillswap.backend.dto.response.BookingResponse;
import com.skillswap.backend.dto.response.SessionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SkillSessionService {
    SessionResponse createSession(String teacherId, SessionCreateRequest request);
    Page<SessionResponse> browseSessions(String skillId, Pageable pageable);
    Page<SessionResponse> getSessionsByTeacher(String teacherId, Pageable pageable);
    SessionResponse getSession(String sessionId);
    void deactivateSession(String sessionId, String teacherId);

    BookingResponse bookSession(String learnerId, String sessionId, SessionBookRequest request);
    List<BookingResponse> getMyBookings(String userId);
    BookingResponse completeBooking(String bookingId, String userId);
    BookingResponse cancelBooking(String bookingId, String userId);
}