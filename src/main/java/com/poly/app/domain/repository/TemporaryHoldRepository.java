package com.poly.app.domain.repository;

import com.poly.app.domain.model.TemporaryHold;
import com.poly.app.infrastructure.constant.HoldStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TemporaryHoldRepository extends JpaRepository<TemporaryHold, Integer> {

    // Tìm holds đã hết hạn
    List<TemporaryHold> findByStatusAndExpireTimeBefore(
            HoldStatus status,
            LocalDateTime time
    );

    // Tìm holds theo billId
    List<TemporaryHold> findByBillId(String billId);

    // Tìm holds theo productId
    List<TemporaryHold> findByProductId(String productId);
}