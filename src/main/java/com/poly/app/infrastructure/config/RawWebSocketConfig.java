package com.poly.app.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Configuration
@EnableWebSocket
public class RawWebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(rawWebSocketHandler(), "/raw-ws")
                .setAllowedOriginPatterns("*");
    }

    @Bean
    public RawWebSocketHandler rawWebSocketHandler() {
        return new RawWebSocketHandler();
    }
}

class RawWebSocketHandler extends TextWebSocketHandler {
    // Lưu danh sách các session đang kết nối
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Thêm session khi client kết nối (tương tự "subscribe")
        sessions.add(session);
        session.sendMessage(new TextMessage("{\"message\": \"Connected to server!\"}"));
        System.out.println("Client connected: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Xử lý tin nhắn từ client (tương tự @MessageMapping("/share-app"))
        String rawJson = message.getPayload();
        System.out.println("Received raw JSON: " + rawJson);

        // Gửi tin nhắn tới tất cả client đang kết nối (tương tự convertAndSend("/topic/apps/share-app"))
        TextMessage broadcastMessage = new TextMessage(rawJson);
        for (WebSocketSession clientSession : sessions) {
            if (clientSession.isOpen()) {
                try {
                    clientSession.sendMessage(broadcastMessage);
                } catch (IOException e) {
                    System.err.println("Error sending message to session " + clientSession.getId() + ": " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        // Xóa session khi client ngắt kết nối
        sessions.remove(session);
        System.out.println("Client disconnected: " + session.getId());
    }

    // Phương thức để gửi tin nhắn từ controller hoặc logic khác (tùy chọn)
    public void sendMessageToAll(String message) throws IOException {
        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession clientSession : sessions) {
            if (clientSession.isOpen()) {
                clientSession.sendMessage(textMessage);
            }
        }
    }
}