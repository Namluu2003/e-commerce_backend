package com.poly.app.domain.app;

import com.poly.app.domain.admin.bill.request.CreateBillRequest;
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
public class AppContronller {
    SimpMessagingTemplate simpMessagingTemplate;
    CommentService commentService;
    ConversationRepository conversationRepository;
    MessageRepository messageRepository;

    // Test gửi tin nhắn qua WebSocket bằng Postman
    @PostMapping("/send-message/test")
    public String sendMessageViaRest() {
        String destination = "/topic/apps/share-app"; // Gửi đến tất cả user

        simpMessagingTemplate.convertAndSend(destination, "Hello tất cả mọi người!");
        System.out.println("Đã gửi tin nhắn tới: " + destination);

        return "Đã gửi tin nhắn thành công!";
    }


    @MessageMapping("/share-app")
    public void sendMessage(@Payload String rawJson) {
        System.out.println("ok");
        simpMessagingTemplate.convertAndSend("/topic/apps/share-app", rawJson);
    }



}