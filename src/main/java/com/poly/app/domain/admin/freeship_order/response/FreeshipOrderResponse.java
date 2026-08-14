package com.poly.app.domain.admin.freeship_order.response;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@Builder
public class FreeshipOrderResponse {
    private Integer id;
    private Double minOrderValue;
    private Double shippingDiscount;


}
