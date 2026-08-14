package com.poly.app.domain.admin.freeship_order.service;

import com.poly.app.domain.model.FreeshipOrder;

public interface FreeshipOrderService {

    // Lấy danh sách tất cả mức freeship
    void setMinOrderValue(Double minOrderValue);

    FreeshipOrder getMinOrderValue();
}
