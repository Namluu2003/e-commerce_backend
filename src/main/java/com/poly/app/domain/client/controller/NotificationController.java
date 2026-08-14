package com.poly.app.domain.client.controller;

import com.poly.app.domain.client.response.NotificationResponse;
import com.poly.app.domain.common.ApiResponse;
import com.poly.app.domain.model.Announcement;
import com.poly.app.domain.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @GetMapping("/history")
    public ApiResponse<List<NotificationResponse>> getNotificationHistory(@RequestParam Integer id) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }

//            String customerIdStr = authentication.getName();
        Long customerId = Long.valueOf(id); // Chuyển đổi an toàn
        List<Announcement> announcements = announcementRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);

        List<NotificationResponse> response = announcements.stream()
                .map(ann -> new NotificationResponse(
                        Long.valueOf(ann.getId()),
                        ann.getAnnouncementContent(),
                        new Date(ann.getCreatedAt()),
                        ann.getIsRead()
                ))
                .collect(Collectors.toList());
        return ApiResponse.<List<NotificationResponse>>builder()
                .data(response)
                .build();
    }

    @GetMapping("/read/{id}")
    public ApiResponse<Announcement> read(@PathVariable("id") Integer id) {
        Announcement announcement = announcementRepository.findById(id).get();
        announcement.setIsRead(true);
        announcementRepository.save(announcement);
        return ApiResponse.<Announcement>builder()
                .data(announcement)
                .build();
    }
}