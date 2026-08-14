package com.poly.app.infrastructure.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimestampConverter {
    public static void main(String[] args) {
        // Timestamp (mili giây)
        long timestamp = 1745236500844L; // Thay bằng giá trị timestamp thực tế của bạn

        // Chuyển đổi timestamp sang ZonedDateTime (múi giờ hệ thống)
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault()); // Múi giờ hệ thống

        // Định dạng lại ZonedDateTime thành chuỗi theo định dạng YYYY-MM-DD HH:MM:SS
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = zonedDateTime.format(formatter);

        // In ra kết quả
        System.out.println(formattedDate);  // Output: 2024-12-28 02:58:48 nếu đúng múi giờ
    }
}

