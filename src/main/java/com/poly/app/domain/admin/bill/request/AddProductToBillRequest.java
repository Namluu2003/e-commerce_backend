package com.poly.app.domain.admin.bill.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddProductToBillRequest {
    private String billCode;          // ID của hóa đơn
    private Integer productDetailId;       // ID của sản phẩm
    private Integer quantity;
}
