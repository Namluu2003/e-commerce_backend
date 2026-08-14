package com.poly.app.domain.admin.bill.service;

import com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendProductUpdate(ProductDetailResponse productDetailResponse) {
        messagingTemplate.convertAndSend("/topic/product-updates", productDetailResponse);
    }



}
