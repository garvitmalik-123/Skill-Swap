package com.skillswap.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseRejectionRequest {

    @NotBlank
    @Size(max = 500)
    private String reason;
}