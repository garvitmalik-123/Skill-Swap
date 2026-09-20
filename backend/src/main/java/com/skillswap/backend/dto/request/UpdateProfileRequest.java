package com.skillswap.backend.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;

    @Size(max = 100)
    private String location;

    private String profileImageUrl;

    private String experienceLevel;
}