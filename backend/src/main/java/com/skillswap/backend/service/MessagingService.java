package com.skillswap.backend.service;

import com.skillswap.backend.dto.response.ConversationResponse;
import com.skillswap.backend.dto.response.MessageResponse;

import java.util.List;

public interface MessagingService {

    MessageResponse sendMessage(String senderId, String receiverId, String content);

    List<ConversationResponse> getMyConversations(String userId);

    List<MessageResponse> getMessages(String conversationId, String userId);

    long getUnreadMessageCount(String userId);
}