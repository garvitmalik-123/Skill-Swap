package com.skillswap.backend.dto.response;

import com.skillswap.backend.entity.Report.ReportStatus;
import com.skillswap.backend.entity.Report.TargetType;
import lombok.*;

import java.time.Instant;

@Data
@Builder
public class ReportResponse {
    private String id;
    private String reporterId;
    private String reporterName;
    private TargetType targetType;
    private String targetId;
    private String reason;
    private String description;
    private ReportStatus status;
    private String resolvedBy;
    private String resolutionNote;
    private Instant resolvedAt;
    private Instant createdAt;
}