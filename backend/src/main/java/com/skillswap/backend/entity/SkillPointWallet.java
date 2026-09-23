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
@Document(collection = "skillpoint_wallets")
public class SkillPointWallet {

    @Id
    private String id;

    @Indexed(unique = true)
    private String userId;

    @Builder.Default
    private int balance = 0;

    /** Optimistic locking — prevents lost updates on concurrent credit/debit. */
    @Version
    private Long version;
}