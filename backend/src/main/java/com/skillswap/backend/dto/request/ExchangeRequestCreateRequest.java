package com.skillswap.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ExchangeRequestCreateRequest {

    @NotBlank
    private String receiverId;

    @NotBlank
    private String senderSkill;    // what you'll teach them

    @NotBlank
    private String receiverSkill;  // what you want to learn from them

    @Size(max = 500)
    private String message;
}