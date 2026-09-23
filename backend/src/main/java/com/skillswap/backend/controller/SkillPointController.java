package com.skillswap.backend.controller;

import com.skillswap.backend.dto.response.ApiResponse;
import com.skillswap.backend.dto.response.SkillPointBalanceResponse;
import com.skillswap.backend.dto.response.SkillPointTransactionResponse;
import com.skillswap.backend.security.CustomUserDetails;
import com.skillswap.backend.service.SkillPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me/skillpoints")
@RequiredArgsConstructor
public class SkillPointController {

    private final SkillPointService skillPointService;

    @GetMapping
    public ResponseEntity<ApiResponse<SkillPointBalanceResponse>> getBalance(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        SkillPointBalanceResponse response = skillPointService.getBalance(currentUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<Page<SkillPointTransactionResponse>>> getTransactions(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Pageable pageable) {
        Page<SkillPointTransactionResponse> transactions =
                skillPointService.getTransactionHistory(currentUser.getUserId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }
}