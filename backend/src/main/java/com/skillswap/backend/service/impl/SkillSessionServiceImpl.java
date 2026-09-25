package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.request.SessionBookRequest;
import com.skillswap.backend.dto.request.SessionCreateRequest;
import com.skillswap.backend.dto.response.BookingResponse;
import com.skillswap.backend.dto.response.SessionResponse;
import com.skillswap.backend.entity.*;
import com.skillswap.backend.entity.Notification.NotificationType;
import com.skillswap.backend.entity.SessionBooking.BookingStatus;
import com.skillswap.backend.entity.SkillSession.SessionStatus;
import com.skillswap.backend.entity.SkillSession.SessionType;
import com.skillswap.backend.exception.BadRequestException;
import com.skillswap.backend.exception.ForbiddenException;
import com.skillswap.backend.exception.ResourceNotFoundException;
import com.skillswap.backend.repository.*;
import com.skillswap.backend.service.NotificationService;
import com.skillswap.backend.service.SkillPointService;
import com.skillswap.backend.service.SkillSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillSessionServiceImpl implements SkillSessionService {

    private final SkillSessionRepository sessionRepository;
    private final SessionBookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final SkillPointService skillPointService;
    private final NotificationService notificationService;

    @Override
    public SessionResponse createSession(String teacherId, SessionCreateRequest request) {
        skillRepository.findById(request.getSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        if (request.getType() == SessionType.SKILLPOINT && request.getSkillPointCost() == null) {
            throw new BadRequestException("skillPointCost is required for SKILLPOINT sessions");
        }
        if (request.getType() == SessionType.PAID && request.getPrice() == null) {
            throw new BadRequestException("price is required for PAID sessions");
        }

        SkillSession session = SkillSession.builder()
                .teacherId(teacherId)
                .skillId(request.getSkillId())
                .title(request.getTitle())
                .description(request.getDescription())
                .durationMinutes(request.getDurationMinutes())
                .type(request.getType())
                .price(request.getPrice())
                .skillPointCost(request.getSkillPointCost())
                .availableSlots(new ArrayList<>(request.getAvailableSlots()))
                .status(SessionStatus.ACTIVE)
                .build();

        return toSessionResponse(sessionRepository.save(session));
    }

    @Override
    public Page<SessionResponse> browseSessions(String skillId, Pageable pageable) {
        Page<SkillSession> page = (skillId == null || skillId.isBlank())
                ? sessionRepository.findByStatus(SessionStatus.ACTIVE, pageable)
                : sessionRepository.findBySkillIdAndStatus(skillId, SessionStatus.ACTIVE, pageable);
        return page.map(this::toSessionResponse);
    }

    @Override
    public Page<SessionResponse> getSessionsByTeacher(String teacherId, Pageable pageable) {
        return sessionRepository.findByTeacherId(teacherId, pageable).map(this::toSessionResponse);
    }

    @Override
    public SessionResponse getSession(String sessionId) {
        SkillSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        return toSessionResponse(session);
    }

    @Override
    public void deactivateSession(String sessionId, String teacherId) {
        SkillSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        if (!session.getTeacherId().equals(teacherId)) {
            throw new ForbiddenException("Only the session creator can deactivate it");
        }

        session.setStatus(SessionStatus.INACTIVE);
        sessionRepository.save(session);
    }

    @Override
    public BookingResponse bookSession(String learnerId, String sessionId, SessionBookRequest request) {
        SkillSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new BadRequestException("This session is no longer active");
        }

        if (session.getTeacherId().equals(learnerId)) {
            throw new BadRequestException("You cannot book your own session");
        }

        if (!session.getAvailableSlots().contains(request.getScheduledAt())) {
            throw new BadRequestException("Selected slot is not available");
        }

        String idempotencyKey = "session-book:" + sessionId + ":" + learnerId + ":" + request.getScheduledAt();

        switch (session.getType()) {
            case FREE -> { /* no payment step */ }
            case SKILLPOINT -> skillPointService.debit(
                    learnerId,
                    session.getSkillPointCost(),
                    SkillPointTransaction.Reason.SESSION_BOOKING,
                    sessionId,
                    "Booked session: " + session.getTitle(),
                    idempotencyKey);
            case PAID -> throw new BadRequestException(
                    "Paid session booking is not yet supported. This feature is coming soon.");
        }

        // Remove the booked slot so it can't be double-booked
        session.getAvailableSlots().remove(request.getScheduledAt());
        sessionRepository.save(session);

        SessionBooking booking = SessionBooking.builder()
                .sessionId(sessionId)
                .teacherId(session.getTeacherId())
                .learnerId(learnerId)
                .scheduledAt(request.getScheduledAt())
                .status(BookingStatus.CONFIRMED)
                .paymentReferenceId(session.getType() == SessionType.SKILLPOINT ? idempotencyKey : null)
                .build();

        SessionBooking saved = bookingRepository.save(booking);

        String learnerName = userRepository.findById(learnerId).map(User::getName).orElse("A learner");
        notificationService.notify(
                session.getTeacherId(),
                NotificationType.SESSION_BOOKING,
                "New session booking",
                learnerName + " booked your session \"" + session.getTitle() + "\"",
                saved.getId(),
                "SESSION_BOOKING");

        return toBookingResponse(saved);
    }

    @Override
    public List<BookingResponse> getMyBookings(String userId) {
        List<SessionBooking> asLearner = bookingRepository.findByLearnerId(userId);
        List<SessionBooking> asTeacher = bookingRepository.findByTeacherId(userId);

        List<SessionBooking> combined = new ArrayList<>(asLearner);
        combined.addAll(asTeacher);

        return combined.stream().map(this::toBookingResponse).toList();
    }

    @Override
    public BookingResponse completeBooking(String bookingId, String userId) {
        SessionBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getTeacherId().equals(userId) && !booking.getLearnerId().equals(userId)) {
            throw new ForbiddenException("You are not part of this booking");
        }

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new BadRequestException("Booking is not in CONFIRMED state");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        booking.setCompletedAt(Instant.now());
        SessionBooking saved = bookingRepository.save(booking);

        SkillSession session = sessionRepository.findById(booking.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        if (session.getType() == SessionType.SKILLPOINT) {
            skillPointService.credit(
                    session.getTeacherId(),
                    session.getSkillPointCost(),
                    SkillPointTransaction.Reason.SESSION_EARNING,
                    booking.getId(),
                    "Earning from completed session: " + session.getTitle(),
                    "session-earn:" + booking.getId());
        }

        notificationService.notify(
                booking.getLearnerId(),
                NotificationType.SESSION_COMPLETED,
                "Session completed",
                "Your session \"" + session.getTitle() + "\" is marked complete",
                saved.getId(),
                "SESSION_BOOKING");

        return toBookingResponse(saved);
    }

    @Override
    public BookingResponse cancelBooking(String bookingId, String userId) {
        SessionBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getTeacherId().equals(userId) && !booking.getLearnerId().equals(userId)) {
            throw new ForbiddenException("You are not part of this booking");
        }

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new BadRequestException("Only confirmed bookings can be cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(Instant.now());
        SessionBooking saved = bookingRepository.save(booking);

        SkillSession session = sessionRepository.findById(booking.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        // Refund the learner if points were spent
        if (session.getType() == SessionType.SKILLPOINT) {
            skillPointService.credit(
                    booking.getLearnerId(),
                    session.getSkillPointCost(),
                    SkillPointTransaction.Reason.SESSION_EARNING, // refund credited under same reason family
                    booking.getId(),
                    "Refund for cancelled session: " + session.getTitle(),
                    "session-refund:" + booking.getId());
        }

        // Return the slot to availability
        session.getAvailableSlots().add(booking.getScheduledAt());
        sessionRepository.save(session);

        return toBookingResponse(saved);
    }

    private SessionResponse toSessionResponse(SkillSession session) {
        String teacherName = userRepository.findById(session.getTeacherId())
                .map(User::getName).orElse("Unknown");
        String skillName = skillRepository.findById(session.getSkillId())
                .map(Skill::getName).orElse("Unknown");

        return SessionResponse.builder()
                .id(session.getId())
                .teacherId(session.getTeacherId())
                .teacherName(teacherName)
                .skillId(session.getSkillId())
                .skillName(skillName)
                .title(session.getTitle())
                .description(session.getDescription())
                .durationMinutes(session.getDurationMinutes())
                .type(session.getType())
                .price(session.getPrice())
                .skillPointCost(session.getSkillPointCost())
                .availableSlots(session.getAvailableSlots())
                .status(session.getStatus())
                .createdAt(session.getCreatedAt())
                .build();
    }

    private BookingResponse toBookingResponse(SessionBooking booking) {
        String sessionTitle = sessionRepository.findById(booking.getSessionId())
                .map(SkillSession::getTitle).orElse("Unknown session");
        String teacherName = userRepository.findById(booking.getTeacherId())
                .map(User::getName).orElse("Unknown");
        String learnerName = userRepository.findById(booking.getLearnerId())
                .map(User::getName).orElse("Unknown");

        return BookingResponse.builder()
                .id(booking.getId())
                .sessionId(booking.getSessionId())
                .sessionTitle(sessionTitle)
                .teacherId(booking.getTeacherId())
                .teacherName(teacherName)
                .learnerId(booking.getLearnerId())
                .learnerName(learnerName)
                .scheduledAt(booking.getScheduledAt())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .completedAt(booking.getCompletedAt())
                .build();
    }
}