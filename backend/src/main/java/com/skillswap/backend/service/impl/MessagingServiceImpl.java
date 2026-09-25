package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.response.ConversationResponse;
import com.skillswap.backend.dto.response.MessageResponse;
import com.skillswap.backend.entity.Conversation;
import com.skillswap.backend.entity.Message;
import com.skillswap.backend.entity.Notification.NotificationType;
import com.skillswap.backend.entity.User;
import com.skillswap.backend.exception.BadRequestException;
import com.skillswap.backend.exception.ForbiddenException;
import com.skillswap.backend.exception.ResourceNotFoundException;
import com.skillswap.backend.repository.ConversationRepository;
import com.skillswap.backend.repository.MessageRepository;
import com.skillswap.backend.repository.UserRepository;
import com.skillswap.backend.service.MessagingService;
import com.skillswap.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessagingServiceImpl implements MessagingService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public MessageResponse sendMessage(String senderId, String receiverId, String content) {
        if (senderId.equals(receiverId)) {
            throw new BadRequestException("You cannot message yourself");
        }

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));

        Conversation conversation = conversationRepository.findByParticipants(senderId, receiverId)
                .orElseGet(() -> conversationRepository.save(
                        Conversation.builder()
                                .participantIds(List.of(senderId, receiverId))
                                .build()));

        Message message = Message.builder()
                .conversationId(conversation.getId())
                .senderId(senderId)
                .content(content)
                .read(false)
                .build();

        Message saved = messageRepository.save(message);

        conversation.setLastMessagePreview(truncate(content, 100));
        conversation.setLastMessageAt(Instant.now());
        conversationRepository.save(conversation);

        User sender = userRepository.findById(senderId).orElse(null);
        notificationService.notify(
                receiverId,
                NotificationType.ADMIN_ACTION, // no dedicated MESSAGE type yet; see note below
                "New message",
                (sender != null ? sender.getName() : "Someone") + " sent you a message",
                conversation.getId(),
                "CONVERSATION");

        return toMessageResponse(saved);
    }

    @Override
    public List<ConversationResponse> getMyConversations(String userId) {
        return conversationRepository.findAllByParticipant(userId).stream()
                .map(c -> toConversationResponse(c, userId))
                .sorted((a, b) -> {
                    if (a.getLastMessageAt() == null) return 1;
                    if (b.getLastMessageAt() == null) return -1;
                    return b.getLastMessageAt().compareTo(a.getLastMessageAt());
                })
                .toList();
    }

    @Override
    public List<MessageResponse> getMessages(String conversationId, String userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        if (!conversation.getParticipantIds().contains(userId)) {
            throw new ForbiddenException("You are not part of this conversation");
        }

        List<Message> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);

        // Mark incoming messages as read
        messages.stream()
                .filter(m -> !m.getSenderId().equals(userId) && !m.isRead())
                .forEach(m -> {
                    m.setRead(true);
                    messageRepository.save(m);
                });

        return messages.stream().map(this::toMessageResponse).toList();
    }

    @Override
    public long getUnreadMessageCount(String userId) {
        return conversationRepository.findAllByParticipant(userId).stream()
                .mapToLong(c -> messageRepository.countByConversationIdAndSenderIdNotAndReadFalse(c.getId(), userId))
                .sum();
    }

    private ConversationResponse toConversationResponse(Conversation conversation, String userId) {
        String otherUserId = conversation.getParticipantIds().stream()
                .filter(id -> !id.equals(userId))
                .findFirst()
                .orElse(null);

        String otherUserName = otherUserId == null ? "Unknown" : userRepository.findById(otherUserId)
                .map(User::getName).orElse("Unknown");

        long unread = messageRepository.countByConversationIdAndSenderIdNotAndReadFalse(conversation.getId(), userId);

        return ConversationResponse.builder()
                .id(conversation.getId())
                .otherUserId(otherUserId)
                .otherUserName(otherUserName)
                .lastMessagePreview(conversation.getLastMessagePreview())
                .lastMessageAt(conversation.getLastMessageAt())
                .unreadCount(unread)
                .build();
    }

    private MessageResponse toMessageResponse(Message message) {
        String senderName = userRepository.findById(message.getSenderId())
                .map(User::getName).orElse("Unknown");

        return MessageResponse.builder()
                .id(message.getId())
                .conversationId(message.getConversationId())
                .senderId(message.getSenderId())
                .senderName(senderName)
                .content(message.getContent())
                .read(message.isRead())
                .createdAt(message.getCreatedAt())
                .build();
    }

    private String truncate(String text, int maxLen) {
        if (text == null) return null;
        return text.length() <= maxLen ? text : text.substring(0, maxLen) + "...";
    }
}