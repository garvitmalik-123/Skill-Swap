package com.skillswap.backend.controller;

import com.skillswap.backend.dto.request.ReportCreateRequest;
import com.skillswap.backend.dto.response.ReportResponse;
import com.skillswap.backend.security.CustomUserDetails;
import com.skillswap.backend.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponse> createReport(
            @Valid @RequestBody ReportCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(reportService.createReport(userDetails.getUserId(), request));
    }

    @GetMapping("/me")
    public ResponseEntity<Page<ReportResponse>> getMyReports(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Pageable pageable) {
        return ResponseEntity.ok(reportService.getMyReports(userDetails.getUserId(), pageable));
    }
}