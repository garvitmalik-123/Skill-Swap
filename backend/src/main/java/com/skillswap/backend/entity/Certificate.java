package com.skillswap.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "certificates")
public class Certificate {

    @Id
    private String id;

    /** Public-facing unique certificate code, safe to share/print (e.g. "SKW-2026-A1B2C3"). */
    @Indexed(unique = true)
    private String certificateCode;

    @Indexed
    private String userId;

    @Indexed
    private String courseId;

    private String userName;

    private String courseTitle;

    private Instant completionDate;

    @CreatedDate
    private Instant issuedAt;

    @Builder.Default
    private boolean verified = true;
}