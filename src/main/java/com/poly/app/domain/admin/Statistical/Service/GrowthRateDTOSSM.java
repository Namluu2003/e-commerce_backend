package com.poly.app.domain.admin.Statistical.Service;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GrowthRateDTOSSM {
    private int year = 0;  // Năm
    private int month = 0;  // Tháng
    private int quantityMonth = 0;  // Số lượng bán tháng hiện tại
    private int lastMonthSold = 0;  // Số lượng bán tháng trước
    private int difference = 0;  // Chênh lệch số lượng
    private String percentageChange = "";  // Lưu % dưới dạng String
}
