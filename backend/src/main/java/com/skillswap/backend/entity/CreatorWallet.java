package com.skillswap.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "wallets")
public class CreatorWallet {

    @Id
    private String id;

    @Indexed(unique = true)
    private String creatorId;

    @Builder.Default
    private double pendingEarnings = 0.0;

    @Builder.Default
    private double availableEarnings = 0.0;

    /** e.g. 0.10 for 10% platform commission. */
    @Builder.Default
    private double commissionRate = 0.10;

    @Version
    private Long version;
}