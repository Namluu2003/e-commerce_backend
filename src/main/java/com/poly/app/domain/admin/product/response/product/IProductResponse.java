package com.poly.app.domain.admin.product.response.product;

import com.poly.app.infrastructure.constant.Status;

public interface IProductResponse {
    Integer getId();
    String getCode();
    String getProductName();
    Integer getTotalQuantity();
    Long getUpdatedAt();
    Byte getStatus();
    Long lastUpdated();
}
