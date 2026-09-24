package com.skillswap.backend.dto.request;

import com.skillswap.backend.entity.SkillSession.SessionType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class SessionCreateRequest {

    @NotBlank
    private String skillId;

    @NotBlank
    private String title;

    @Size(max = 1000)
    private String description;

    @NotNull @Min(15)
    private Integer durationMinutes;

    @NotNull
    private SessionType type;

    private Double price;           // required if type == PAID
    private Integer skillPointCost; // required if type == SKILLPOINT

    @NotEmpty
    private List<Instant> availableSlots;
}