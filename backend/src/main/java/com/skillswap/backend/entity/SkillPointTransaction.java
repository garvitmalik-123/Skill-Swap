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
@Document(collection = "skillpoint_transactions")
public class SkillPointTransaction {

    @Id
    private String id;

    @Indexed
    private String userId;

    private TransactionType type;

    /** Always positive; direction is determined by type. */
    private int amount;

    private int balanceAfter;

    private Reason reason;

    private String referenceId;

    private String description;

    /** Prevents the same external event from being applied twice. */
    @Indexed(unique = true, sparse = true)
    private String idempotencyKey;

    @CreatedDate
    private Instant createdAt;

    public enum TransactionType {
        CREDIT, DEBIT
    }

    public enum Reason {
        TEACHING_REWARD,
        COURSE_CONTRIBUTION,
        COMMUNITY_CONTRIBUTION,
        CHALLENGE_REWARD,
        COURSE_ENROLLMENT,
        SKILL_SESSION,
        SESSION_BOOKING,
        SESSION_EARNING,
        PLATFORM_ACTIVITY,
        ADMIN_ADJUSTMENT
    }
}