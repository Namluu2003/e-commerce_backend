package com.poly.app.domain.admin.freeship_order.service.impl;
import com.poly.app.domain.admin.freeship_order.service.FreeshipOrderService;
import com.poly.app.domain.model.FreeshipOrder;
import com.poly.app.domain.repository.FreeshipOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FreeshipOrderServiceImpl implements FreeshipOrderService {

    private final FreeshipOrderRepository freeshipOrderRepository;

    @Override
    public void setMinOrderValue(Double minOrderValue) {
        if (minOrderValue == null || minOrderValue < 0) {
            throw new IllegalArgumentException("Giá trị đơn hàng tối thiểu phải lớn hơn hoặc bằng 0");
        }
        // Giả sử chỉ có một bản ghi cấu hình freeship duy nhất
        FreeshipOrder freeshipOrder = freeshipOrderRepository.findTopByOrderByIdDesc();
        if (freeshipOrder == null) {
            freeshipOrder = new FreeshipOrder();
        }
        freeshipOrder.setMinOrderValue(minOrderValue);
        freeshipOrderRepository.save(freeshipOrder);
    }

    @Override
    public FreeshipOrder getMinOrderValue() {
        return freeshipOrderRepository.findTopByOrderByIdDesc();
    }

}
