package com.poly.app.domain.client.controller;

import com.poly.app.domain.client.service.ZaloPayStatusChecker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/polling")
public class PollingController {
    private final ZaloPayStatusChecker checker;

    public PollingController(ZaloPayStatusChecker checker) {
        this.checker = checker;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startPolling() {
        checker.startPolling();
        return ResponseEntity.ok("✅ Polling đã bật!");
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopPolling() {
        checker.stopPolling();
        return ResponseEntity.ok("⏸ Polling đã tắt!");
    }
}
