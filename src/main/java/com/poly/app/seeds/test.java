package com.poly.app.seeds;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class test {
    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println("Current Time in Millis: " + currentTimeMillis);
        Instant instant = Instant.now();
        System.out.println("Current Instant: " + instant);
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("Current Local Date and Time: " + localDateTime);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        System.out.println("Current Date and Time in Vietnam: " + zonedDateTime);
        Calendar calendar = Calendar.getInstance();
        System.out.println("Current Date and Time: " + calendar.getTime());
        Date date = new Date();
        System.out.println("Current Date: " + date);
    }
}
