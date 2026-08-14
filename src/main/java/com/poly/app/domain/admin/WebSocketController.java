package com.poly.app.domain.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class WebSocketController {

    @MessageMapping("/updateQuantity")
    @SendTo("/topic/product-quantity")
    public String sendProductUpdate(String message) {
        log.info("cHẠY VÀO ĐÂY");
        return message;
    }
}
