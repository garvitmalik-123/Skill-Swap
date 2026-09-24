package com.skillswap.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class SessionBookRequest {

    @NotNull
    private Instant scheduledAt; // must be one of the session's availableSlots
}