package com.skillswap.backend.dto.request;

import com.skillswap.backend.entity.Report.TargetType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReportCreateRequest {

    @NotNull
    private TargetType targetType;

    @NotBlank
    private String targetId;

    @NotBlank
    @Size(max = 200)
    private String reason;

    @Size(max = 1000)
    private String description;
}