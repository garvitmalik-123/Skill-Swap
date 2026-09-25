package com.skillswap.backend.controller;

import com.skillswap.backend.dto.request.MessageSendRequest;
import com.skillswap.backend.dto.response.MessageResponse;
import com.skillswap.backend.security.CustomUserDetails;
import com.skillswap.backend.service.MessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageWebSocketController {

    private final MessagingService messagingService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(MessageSendRequest request, Authentication authentication) {
        String senderId = extractUserId(authentication);

        MessageResponse response = messagingService.sendMessage(senderId, request.getReceiverId(), request.getContent());

        messagingTemplate.convertAndSendToUser(request.getReceiverId(), "/queue/messages", response);
        messagingTemplate.convertAndSendToUser(senderId, "/queue/messages", response);
    }

    private String extractUserId(Authentication authentication) {
        if (authentication instanceof UsernamePasswordAuthenticationToken token
                && token.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUserId();
        }
        throw new IllegalStateException("Unable to resolve authenticated user from WebSocket session");
    }
}