package com.poly.app.domain.admin.Statistical.Service;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class NameData {
    private String reportTime;  // Có thể là ngày tuần, tháng, năm hoặc ngày cụ thể
    private double totalRevenue;
    private int totalOrders;
    private int successfulOrders;
    private int cancelledOrders;
    private int totalProductsSold;

    public NameData(String reportTime, double totalRevenue, int totalOrders,
                    int successfulOrders, int cancelledOrders,
                    int totalProductsSold) {
        this.reportTime = reportTime;
        this.totalRevenue = totalRevenue;
        this.totalOrders = totalOrders;
        this.successfulOrders = successfulOrders;
        this.cancelledOrders = cancelledOrders;
        this.totalProductsSold = totalProductsSold;
    }
}
