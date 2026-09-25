package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.request.ExchangeRequestCreateRequest;
import com.skillswap.backend.dto.response.ExchangeRequestResponse;
import com.skillswap.backend.entity.Notification.NotificationType;
import com.skillswap.backend.entity.SkillExchangeRequest;
import com.skillswap.backend.entity.SkillExchangeRequest.ExchangeStatus;
import com.skillswap.backend.entity.User;
import com.skillswap.backend.exception.*;
import com.skillswap.backend.repository.SkillExchangeRequestRepository;
import com.skillswap.backend.repository.UserRepository;
import com.skillswap.backend.service.NotificationService;
import com.skillswap.backend.service.SkillExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillExchangeServiceImpl implements SkillExchangeService {

    private final SkillExchangeRequestRepository exchangeRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public ExchangeRequestResponse sendRequest(String senderId, ExchangeRequestCreateRequest request) {
        if (senderId.equals(request.getReceiverId())) {
            throw new ValidationException("You cannot send a request to yourself");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        boolean duplicate = exchangeRepository.existsBySenderIdAndReceiverIdAndStatus(
                senderId, request.getReceiverId(), ExchangeStatus.PENDING);
        if (duplicate) {
            throw new DuplicateResourceException("You already have a pending request with this user");
        }

        SkillExchangeRequest exchange = SkillExchangeRequest.builder()
                .senderId(senderId)
                .receiverId(request.getReceiverId())
                .senderSkill(request.getSenderSkill())
                .receiverSkill(request.getReceiverSkill())
                .message(request.getMessage())
                .status(ExchangeStatus.PENDING)
                .build();

        SkillExchangeRequest saved = exchangeRepository.save(exchange);

        notificationService.notify(
                request.getReceiverId(),
                NotificationType.SKILL_EXCHANGE_REQUEST,
                "New skill exchange request",
                sender.getName() + " wants to exchange skills with you",
                saved.getId(),
                "SKILL_EXCHANGE_REQUEST");

        return mapToResponse(saved);
    }

    @Override
    public List<ExchangeRequestResponse> getMyRequests(String userId) {
        return exchangeRepository.findBySenderIdOrReceiverId(userId, userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ExchangeRequestResponse acceptRequest(String requestId, String userId) {
        SkillExchangeRequest exchange = getOwnedByReceiver(requestId, userId);
        validateStatus(exchange, ExchangeStatus.PENDING);
        exchange.setStatus(ExchangeStatus.ACCEPTED);
        SkillExchangeRequest saved = exchangeRepository.save(exchange);

        notifyResponse(saved, "accepted");
        return mapToResponse(saved);
    }

    @Override
    public ExchangeRequestResponse rejectRequest(String requestId, String userId) {
        SkillExchangeRequest exchange = getOwnedByReceiver(requestId, userId);
        validateStatus(exchange, ExchangeStatus.PENDING);
        exchange.setStatus(ExchangeStatus.REJECTED);
        SkillExchangeRequest saved = exchangeRepository.save(exchange);

        notifyResponse(saved, "rejected");
        return mapToResponse(saved);
    }

    @Override
    public ExchangeRequestResponse completeExchange(String requestId, String userId) {
        SkillExchangeRequest exchange = exchangeRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange request not found"));

        boolean isParticipant = exchange.getSenderId().equals(userId) || exchange.getReceiverId().equals(userId);
        if (!isParticipant) {
            throw new ForbiddenException("You are not part of this exchange");
        }

        validateStatus(exchange, ExchangeStatus.ACCEPTED);
        exchange.setStatus(ExchangeStatus.COMPLETED);
        exchange.setCompletedAt(Instant.now());
        return mapToResponse(exchangeRepository.save(exchange));
    }

    private void notifyResponse(SkillExchangeRequest exchange, String action) {
        String receiverName = userRepository.findById(exchange.getReceiverId())
                .map(User::getName).orElse("The user");

        notificationService.notify(
                exchange.getSenderId(),
                NotificationType.EXCHANGE_RESPONSE,
                "Exchange request " + action,
                receiverName + " " + action + " your skill exchange request",
                exchange.getId(),
                "SKILL_EXCHANGE_REQUEST");
    }

    private SkillExchangeRequest getOwnedByReceiver(String requestId, String userId) {
        SkillExchangeRequest exchange = exchangeRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange request not found"));

        if (!exchange.getReceiverId().equals(userId)) {
            throw new ForbiddenException("Only the receiver can perform this action");
        }
        return exchange;
    }

    private void validateStatus(SkillExchangeRequest exchange, ExchangeStatus expected) {
        if (exchange.getStatus() != expected) {
            throw new ValidationException("Request is not in " + expected + " state");
        }
    }

    private ExchangeRequestResponse mapToResponse(SkillExchangeRequest exchange) {
        String senderName = userRepository.findById(exchange.getSenderId())
                .map(User::getName).orElse("Unknown");
        String receiverName = userRepository.findById(exchange.getReceiverId())
                .map(User::getName).orElse("Unknown");

        return ExchangeRequestResponse.builder()
                .id(exchange.getId())
                .senderId(exchange.getSenderId())
                .senderName(senderName)
                .receiverId(exchange.getReceiverId())
                .receiverName(receiverName)
                .senderSkill(exchange.getSenderSkill())
                .receiverSkill(exchange.getReceiverSkill())
                .message(exchange.getMessage())
                .status(exchange.getStatus())
                .createdAt(exchange.getCreatedAt())
                .completedAt(exchange.getCompletedAt())
                .build();
    }
}