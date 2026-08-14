package com.poly.app.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class WebSocketTestController {

    private final RawWebSocketHandler webSocketHandler;

    @Autowired
    public WebSocketTestController(RawWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @PostMapping("/test-ws")
    public ResponseEntity<String> sendTestMessage(@RequestBody String message) {
        try {
            webSocketHandler.sendMessageToAll(message);
            return ResponseEntity.ok("Message sent to all WebSocket clients: " + message);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to send message: " + e.getMessage());
        }
    }
}