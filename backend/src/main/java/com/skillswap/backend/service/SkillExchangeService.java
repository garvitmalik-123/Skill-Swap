package com.skillswap.backend.service;

import com.skillswap.backend.dto.request.ExchangeRequestCreateRequest;
import com.skillswap.backend.dto.response.ExchangeRequestResponse;

import java.util.List;

public interface SkillExchangeService {
    ExchangeRequestResponse sendRequest(String senderId, ExchangeRequestCreateRequest request);
    List<ExchangeRequestResponse> getMyRequests(String userId);
    ExchangeRequestResponse acceptRequest(String requestId, String userId);
    ExchangeRequestResponse rejectRequest(String requestId, String userId);
    ExchangeRequestResponse completeExchange(String requestId, String userId);
}