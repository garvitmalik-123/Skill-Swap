package com.skillswap.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateResponse {

    private String id;
    private String certificateCode;
    private String userId;
    private String userName;
    private String courseId;
    private String courseTitle;
    private Instant completionDate;
    private Instant issuedAt;
    private boolean verified;
}