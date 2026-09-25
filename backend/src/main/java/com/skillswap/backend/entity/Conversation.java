package com.skillswap.backend.entity;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "conversations")
public class Conversation {

    @Id
    private String id;

    @Indexed
    private List<String> participantIds; // exactly 2 for 1-to-1 chat

    private String lastMessagePreview;
    private Instant lastMessageAt;

    @CreatedDate
    private Instant createdAt;
}