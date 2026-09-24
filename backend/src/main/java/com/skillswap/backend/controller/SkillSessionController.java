package com.skillswap.backend.controller;

import com.skillswap.backend.dto.request.SessionBookRequest;
import com.skillswap.backend.dto.request.SessionCreateRequest;
import com.skillswap.backend.dto.response.BookingResponse;
import com.skillswap.backend.dto.response.SessionResponse;
import com.skillswap.backend.security.CustomUserDetails;
import com.skillswap.backend.service.SkillSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skill-sessions")
@RequiredArgsConstructor
public class SkillSessionController {

    private final SkillSessionService skillSessionService;

    @PostMapping
    public ResponseEntity<SessionResponse> createSession(
            @Valid @RequestBody SessionCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(skillSessionService.createSession(userDetails.getUserId(), request));
    }

    @GetMapping
    public ResponseEntity<Page<SessionResponse>> browseSessions(
            @RequestParam(required = false) String skillId,
            Pageable pageable) {
        return ResponseEntity.ok(skillSessionService.browseSessions(skillId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionResponse> getSession(@PathVariable String id) {
        return ResponseEntity.ok(skillSessionService.getSession(id));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateSession(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        skillSessionService.deactivateSession(id, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/book")
    public ResponseEntity<BookingResponse> bookSession(
            @PathVariable String id,
            @Valid @RequestBody SessionBookRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(skillSessionService.bookSession(userDetails.getUserId(), id, request));
    }

    @GetMapping("/bookings/me")
    public ResponseEntity<List<BookingResponse>> getMyBookings(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(skillSessionService.getMyBookings(userDetails.getUserId()));
    }

    @PutMapping("/bookings/{id}/complete")
    public ResponseEntity<BookingResponse> completeBooking(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(skillSessionService.completeBooking(id, userDetails.getUserId()));
    }

    @PutMapping("/bookings/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(skillSessionService.cancelBooking(id, userDetails.getUserId()));
    }
}