package com.poly.app.domain.client.controller;

import com.poly.app.domain.model.Message;
import com.poly.app.domain.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;


    @GetMapping("/{conversationId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long conversationId) {
        List<Message> messages = messageRepository.findByConversationId(conversationId);
        return ResponseEntity.ok(messages);
    }

    @Transactional
    @GetMapping("read/{conversationId}")
    public String read(@PathVariable Integer conversationId) {
        messageRepository.read(conversationId);
        return "ok";
    }
}