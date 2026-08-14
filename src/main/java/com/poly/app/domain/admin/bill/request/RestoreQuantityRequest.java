package com.poly.app.domain.admin.bill.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Backend
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestoreQuantityRequest {
    private Integer id;          // ID của sản phẩm
    private Integer quantity;   // Số lượng cần hoàn trả
    private Boolean isRestoreQuantity = false;
}