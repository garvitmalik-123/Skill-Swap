package com.skillswap.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MessageSendRequest {

    @NotBlank
    private String receiverId; // used when starting/continuing a conversation by user, not conversationId

    @NotBlank
    @Size(max = 2000)
    private String content;
}