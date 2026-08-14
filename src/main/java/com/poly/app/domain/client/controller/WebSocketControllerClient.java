package com.poly.app.domain.client.controller;

import com.poly.app.domain.admin.promotion.response.ApiResponse;
import com.poly.app.domain.client.repository.CommentDTO;


import com.poly.app.domain.client.request.MessageRequet;
import com.poly.app.domain.client.response.CommentMessage;
import com.poly.app.domain.client.service.CommentService;
import com.poly.app.domain.model.Conversation;
import com.poly.app.domain.model.Message;
import com.poly.app.domain.repository.ConversationRepository;
import com.poly.app.domain.repository.MessageRepository;
import com.poly.app.infrastructure.constant.MessageStatus;
import com.poly.app.infrastructure.constant.SenderType;
import com.poly.app.infrastructure.constant.Status;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketControllerClient {
    SimpMessagingTemplate simpMessagingTemplate;
    CommentService commentService;
    ConversationRepository conversationRepository;
    MessageRepository messageRepository;

    // Test gửi tin nhắn qua WebSocket bằng Postman
    @PostMapping("/send-message")
    public String sendMessageViaRest() {
        String destination = "/topic/global-notifications"; // Gửi đến tất cả user

        simpMessagingTemplate.convertAndSend(destination, "Hello tất cả mọi người!");
        System.out.println("Đã gửi tin nhắn tới: " + destination);

        return "Đã gửi tin nhắn thành công!";
    }

    @GetMapping("/user-principal")
    public Principal getPrincipal(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User Principal: " + authentication.getPrincipal());
        return principal;
    }


    @MessageMapping("/comment/{productId}")
//    @SendTo("/topic/comments/{productId}")
    public void handleComment(@DestinationVariable Integer productId, CommentMessage message) {
        commentService.saveComment(productId, message.getCustomerId(), message.getComment(), message.getRate(), message.getParentId(),message.getAction());

    }
    @MessageMapping("/message/{conversationId}")
    @SendTo("/topic/messages/{conversationId}")
    public Message sendMessage(@DestinationVariable Integer conversationId, MessageRequet message) {
        // Kiểm tra conversationId có tồn tại không
        Optional<Conversation> conversationOpt = conversationRepository.findById(conversationId);
        if (conversationOpt.isEmpty()) {
            throw new IllegalArgumentException("Cuộc trò chuyện không tồn tại với ID: " + conversationId);
        }

        Message savedMessage = messageRepository.save(Message.builder()
                .senderType(SenderType.valueOf(message.getSenderType()))
                .conversation(conversationOpt.get())
                .content(message.getContent())
                .status(MessageStatus.valueOf(message.getStatus()))
                .senderId(message.getSenderId())
                .build());

        simpMessagingTemplate.convertAndSend("/topic/anou","thong báo mới");

        // Trả về tin nhắn đã lưu để gửi qua WebSocket
        return savedMessage;
    }



}