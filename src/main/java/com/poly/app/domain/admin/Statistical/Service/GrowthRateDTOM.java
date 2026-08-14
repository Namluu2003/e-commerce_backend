package com.poly.app.domain.admin.Statistical.Service;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GrowthRateDTOM {
    private int year = 0;
    private int month = 0;
    private double revenue = 0.0;
    private double lastMonthRevenue = 0.0; // Đổi tên từ lastYearRevenue
    private double difference = 0.0;
    private String percentageChange = "";


}
