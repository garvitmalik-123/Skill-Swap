package com.skillswap.backend.entity;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reports")
public class Report {

    @Id
    private String id;

    @Indexed
    private String reporterId;

    private TargetType targetType;

    @Indexed
    private String targetId;

    private String reason;
    private String description;

    @Builder.Default
    private ReportStatus status = ReportStatus.PENDING;

    private String resolvedBy;
    private String resolutionNote;
    private Instant resolvedAt;

    @CreatedDate
    private Instant createdAt;

    public enum TargetType {
        USER, COURSE, REVIEW, SESSION, CONTENT
    }

    public enum ReportStatus {
        PENDING, RESOLVED, DISMISSED
    }
}