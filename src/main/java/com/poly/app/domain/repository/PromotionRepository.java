package com.poly.app.domain.repository;

import com.poly.app.domain.admin.promotion.response.PromotionResponse;
import com.poly.app.domain.model.Promotion;
import com.poly.app.domain.model.StatusEnum;
import com.poly.app.domain.model.Voucher;
import com.poly.app.infrastructure.constant.VoucherType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {





    List<Promotion> findByStartDateBeforeAndEndDateAfterAndQuantityGreaterThan(
            LocalDateTime today, LocalDateTime todayAgain, Integer quantity
    );
    Page<Promotion> findAll(Specification<Promotion> spec, Pageable pageable);


    List<Promotion> findByEndDateBeforeAndStatusPromotionNot(LocalDateTime today, StatusEnum statusPromotion);
    List<Promotion> findByStartDateBeforeAndStatusPromotionNot(LocalDateTime today, StatusEnum statusPromotion);

}
