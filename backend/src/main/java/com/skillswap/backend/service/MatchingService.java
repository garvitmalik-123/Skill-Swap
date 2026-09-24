package com.skillswap.backend.service;

import com.skillswap.backend.dto.response.MatchResponse;

import java.util.List;

public interface MatchingService {
    List<MatchResponse> findMatches(String userId);
}