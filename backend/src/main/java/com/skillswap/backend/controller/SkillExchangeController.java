package com.skillswap.backend.controller;

import com.skillswap.backend.dto.request.ExchangeRequestCreateRequest;
import com.skillswap.backend.dto.response.ExchangeRequestResponse;
import com.skillswap.backend.dto.response.MatchResponse;
import com.skillswap.backend.security.CustomUserDetails;
import com.skillswap.backend.service.MatchingService;
import com.skillswap.backend.service.SkillExchangeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skill-exchange")
@RequiredArgsConstructor
public class SkillExchangeController {

    private final MatchingService matchingService;
    private final SkillExchangeService skillExchangeService;

    @GetMapping("/matches")
    public ResponseEntity<List<MatchResponse>> getMatches(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(matchingService.findMatches(userDetails.getUserId()));
    }

    @PostMapping("/requests")
    public ResponseEntity<ExchangeRequestResponse> sendRequest(
            @Valid @RequestBody ExchangeRequestCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(skillExchangeService.sendRequest(userDetails.getUserId(), request));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<ExchangeRequestResponse>> getMyRequests(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(skillExchangeService.getMyRequests(userDetails.getUserId()));
    }

    @PutMapping("/requests/{id}/accept")
    public ResponseEntity<ExchangeRequestResponse> accept(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(skillExchangeService.acceptRequest(id, userDetails.getUserId()));
    }

    @PutMapping("/requests/{id}/reject")
    public ResponseEntity<ExchangeRequestResponse> reject(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(skillExchangeService.rejectRequest(id, userDetails.getUserId()));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ExchangeRequestResponse> complete(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(skillExchangeService.completeExchange(id, userDetails.getUserId()));
    }
}