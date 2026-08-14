package com.poly.app.domain.admin.freeship_order.controller;


import com.poly.app.domain.admin.freeship_order.request.FreeshipOrderRequest;
import com.poly.app.domain.admin.freeship_order.service.FreeshipOrderService;
import com.poly.app.domain.model.FreeshipOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin/freeship-order")
public class FreeShipOrderController {

    @Autowired
    FreeshipOrderService freeshipOrderService;

    @PostMapping
    public ResponseEntity<?> handleSetMinOrder(@RequestBody FreeshipOrderRequest request) {
        freeshipOrderService.setMinOrderValue(request.getMinOrderValue());
        return ResponseEntity.ok("success");
    }

    @GetMapping
    public ResponseEntity<?> getMinOrderValueFreeShip() {
        FreeshipOrder freeshipOrder = freeshipOrderService.getMinOrderValue();
        return ResponseEntity.ok(freeshipOrder);
    }

}
