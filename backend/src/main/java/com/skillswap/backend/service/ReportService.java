package com.skillswap.backend.service;

import com.skillswap.backend.dto.request.ReportCreateRequest;
import com.skillswap.backend.dto.request.ReportResolveRequest;
import com.skillswap.backend.dto.response.ReportResponse;
import com.skillswap.backend.entity.Report.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {

    ReportResponse createReport(String reporterId, ReportCreateRequest request);
    Page<ReportResponse> getMyReports(String reporterId, Pageable pageable);

    // Admin-facing
    Page<ReportResponse> getAllReports(ReportStatus status, Pageable pageable);
    ReportResponse resolveReport(String reportId, String adminId, ReportResolveRequest request);
}