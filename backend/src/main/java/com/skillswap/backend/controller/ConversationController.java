package com.skillswap.backend.controller;

import com.skillswap.backend.dto.request.MessageSendRequest;
import com.skillswap.backend.dto.response.ConversationResponse;
import com.skillswap.backend.dto.response.MessageResponse;
import com.skillswap.backend.security.CustomUserDetails;
import com.skillswap.backend.service.MessagingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final MessagingService messagingService;

    @PostMapping("/messages")
    public ResponseEntity<MessageResponse> sendMessage(
            @Valid @RequestBody MessageSendRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(
                messagingService.sendMessage(userDetails.getUserId(), request.getReceiverId(), request.getContent()));
    }

    @GetMapping
    public ResponseEntity<List<ConversationResponse>> getMyConversations(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(messagingService.getMyConversations(userDetails.getUserId()));
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(messagingService.getMessages(id, userDetails.getUserId()));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(messagingService.getUnreadMessageCount(userDetails.getUserId()));
    }
}