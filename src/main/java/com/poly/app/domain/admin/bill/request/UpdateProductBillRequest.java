package com.poly.app.domain.admin.bill.request;

import com.poly.app.domain.admin.product.request.productdetail.ProductDetailRequest;
import com.poly.app.domain.model.ProductDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
public class UpdateProductBillRequest {

    List<BillProductDetailRequest> productDetailRequestList;

    private Double shipMoney;
    // Tiền sau giảm giá
    Double totalMoney; // Tổng
    // Tiền trước áp phiếu giảm và đây là tiền hàng
    // Tiền giảm giá
    private Double moneyBeforeDiscount;
    Double discountMoney;
    String voucherCode;
    Boolean isFreeShip;

}
