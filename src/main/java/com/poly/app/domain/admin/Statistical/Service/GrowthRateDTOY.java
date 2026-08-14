package com.poly.app.domain.admin.Statistical.Service;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GrowthRateDTOY {
    private int year = 0;
    private double revenue = 0.0;
    private double lastYearRevenue = 0.0;
    private double difference = 0.0;
    private String percentageChange = "";

    public GrowthRateDTOY(int year, Double revenue, Double lastYearRevenue, Double difference, String percentageChange) {
        this.year = year;
        this.revenue = (revenue != null) ? revenue : 0.0;
        this.lastYearRevenue = (lastYearRevenue != null) ? lastYearRevenue : 0.0;
        this.difference = (difference != null) ? difference : 0.0;
        this.percentageChange = (percentageChange != null) ? percentageChange : "";
    }

}
