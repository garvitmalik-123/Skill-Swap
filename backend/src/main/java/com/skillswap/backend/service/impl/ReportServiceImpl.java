package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.request.ReportCreateRequest;
import com.skillswap.backend.dto.request.ReportResolveRequest;
import com.skillswap.backend.dto.response.ReportResponse;
import com.skillswap.backend.entity.Notification.NotificationType;
import com.skillswap.backend.entity.Report;
import com.skillswap.backend.entity.Report.ReportStatus;
import com.skillswap.backend.entity.User;
import com.skillswap.backend.exception.BadRequestException;
import com.skillswap.backend.exception.ResourceNotFoundException;
import com.skillswap.backend.repository.ReportRepository;
import com.skillswap.backend.repository.UserRepository;
import com.skillswap.backend.service.NotificationService;
import com.skillswap.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public ReportResponse createReport(String reporterId, ReportCreateRequest request) {
        Report report = Report.builder()
                .reporterId(reporterId)
                .targetType(request.getTargetType())
                .targetId(request.getTargetId())
                .reason(request.getReason())
                .description(request.getDescription())
                .status(ReportStatus.PENDING)
                .build();

        return toResponse(reportRepository.save(report));
    }

    @Override
    public Page<ReportResponse> getMyReports(String reporterId, Pageable pageable) {
        return reportRepository.findByReporterId(reporterId, pageable).map(this::toResponse);
    }

    @Override
    public Page<ReportResponse> getAllReports(ReportStatus status, Pageable pageable) {
        Page<Report> page = (status == null)
                ? reportRepository.findAllByOrderByCreatedAtDesc(pageable)
                : reportRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        return page.map(this::toResponse);
    }

    @Override
    public ReportResponse resolveReport(String reportId, String adminId, ReportResolveRequest request) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        if (report.getStatus() != ReportStatus.PENDING) {
            throw new BadRequestException("This report has already been " + report.getStatus().name().toLowerCase());
        }

        if (request.getStatus() == ReportStatus.PENDING) {
            throw new BadRequestException("Cannot resolve a report back to PENDING");
        }

        report.setStatus(request.getStatus());
        report.setResolvedBy(adminId);
        report.setResolutionNote(request.getResolutionNote());
        report.setResolvedAt(Instant.now());

        Report saved = reportRepository.save(report);

        notificationService.notify(
                report.getReporterId(),
                NotificationType.ADMIN_ACTION,
                "Report update",
                "Your report has been " + request.getStatus().name().toLowerCase()
                        + (request.getResolutionNote() != null ? ": " + request.getResolutionNote() : ""),
                report.getId(),
                "REPORT");

        return toResponse(saved);
    }

    private ReportResponse toResponse(Report report) {
        String reporterName = userRepository.findById(report.getReporterId())
                .map(User::getName).orElse("Unknown");

        return ReportResponse.builder()
                .id(report.getId())
                .reporterId(report.getReporterId())
                .reporterName(reporterName)
                .targetType(report.getTargetType())
                .targetId(report.getTargetId())
                .reason(report.getReason())
                .description(report.getDescription())
                .status(report.getStatus())
                .resolvedBy(report.getResolvedBy())
                .resolutionNote(report.getResolutionNote())
                .resolvedAt(report.getResolvedAt())
                .createdAt(report.getCreatedAt())
                .build();
    }
}