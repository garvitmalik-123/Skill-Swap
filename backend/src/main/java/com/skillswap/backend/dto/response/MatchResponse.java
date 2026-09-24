package com.skillswap.backend.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
public class MatchResponse {
    private String userId;
    private String name;
    private String bio;
    private List<String> theyCanTeach;     // skills matching what you want to learn
    private List<String> theyWantToLearn;  // skills matching what you can teach
    private double compatibilityScore;     // 0.0–1.0
}