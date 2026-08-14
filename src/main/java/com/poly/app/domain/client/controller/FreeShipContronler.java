package com.poly.app.domain.client.controller;

import com.poly.app.domain.admin.freeship_order.service.FreeshipOrderService;
import com.poly.app.domain.client.repository.CommentDTO;
import com.poly.app.domain.client.response.CommentMessage;
import com.poly.app.domain.client.service.CommentService;
import com.poly.app.domain.common.ApiResponse;
import com.poly.app.domain.model.FreeshipOrder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client/free-ship")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FreeShipContronler {
    FreeshipOrderService freeshipOrderService;
    @GetMapping
    public ApiResponse<?> getMinOrderValueFreeShip() {
        FreeshipOrder freeshipOrder = freeshipOrderService.getMinOrderValue();
        return ApiResponse.builder()
                .data(freeshipOrder)
                .build();
    }

}
