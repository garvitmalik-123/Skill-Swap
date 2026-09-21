package com.skillswap.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class LessonRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String content;

    @NotNull(message = "Order is required")
    @PositiveOrZero
    private Integer order;

    private String videoUrl;
    private String resourceUrl;
    private Integer durationMinutes;
}