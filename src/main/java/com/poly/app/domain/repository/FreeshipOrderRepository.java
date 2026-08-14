package com.poly.app.domain.repository;

import com.poly.app.domain.model.FreeshipOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FreeshipOrderRepository extends JpaRepository<FreeshipOrder, Integer> {

    // Tìm mức freeship phù hợp nhất theo tổng giá trị đơn hàng
    Optional<FreeshipOrder> findTopByMinOrderValueLessThanEqualOrderByMinOrderValueDesc(Double totalPrice);

    FreeshipOrder findTopByOrderByIdDesc();
}