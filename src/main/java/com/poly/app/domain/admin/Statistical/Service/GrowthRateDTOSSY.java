package com.poly.app.domain.admin.Statistical.Service;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GrowthRateDTOSSY {
    private int year = 0; // Năm
    private int quantityYear = 0; // Số lượng bán năm hiện tại
    private int lastYearSold = 0; // Số lượng bán năm trước
    private int difference = 0; // Số lượng chênh lệch
    private String yearPercentageChange = ""; // Lưu % dưới dạng String
}
