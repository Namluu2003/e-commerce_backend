package com.poly.app.domain.repository;

import com.poly.app.domain.admin.voucher.response.VoucherResponse;
import com.poly.app.domain.model.StatusEnum;
import com.poly.app.domain.model.Voucher;
import com.poly.app.infrastructure.constant.VoucherType;
import com.poly.app.domain.admin.voucher.response.VoucherReponse;
import com.poly.app.infrastructure.constant.VoucherType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    @Query(value = """
            (SELECT v.* 
            FROM voucher v
            WHERE v.start_date <= :currentTime 
              AND v.end_date >= :currentTime 
              AND v.quantity > 0
              AND v.status_voucher = 'dang_kich_hoat'
              AND v.voucher_type = 'PUBLIC')

            UNION

            (SELECT v.* 
            FROM voucher v
            LEFT JOIN customer_voucher cv ON v.id = cv.voucher_id
            WHERE v.start_date <= :currentTime 
              AND v.end_date >= :currentTime  
              AND cv.quantity > 0
              AND v.status_voucher = 'dang_kich_hoat'
              AND (:customerId IS NOT NULL AND cv.customer_id = :customerId))
            """, nativeQuery = true)
    List<Voucher> findValidVouchers(@Param("customerId") String customerId,@Param("currentTime") Timestamp currentTime);



    List<Voucher> findByStartDateBeforeAndEndDateAfterAndQuantityGreaterThanAndVoucherType
            (LocalDateTime startDate, LocalDateTime endDate, Integer quantity, VoucherType voucherType);


    Page<Voucher> findAll(Specification<Voucher> spec, Pageable pageable);
    Voucher findVoucherByVoucherCode(String voucherCode);


    List<Voucher> findByStartDateBeforeAndStatusVoucher(LocalDateTime date, StatusEnum status);

    List<Voucher> findByEndDateBeforeAndStatusVoucherNotOrQuantity(LocalDateTime endDateBefore, StatusEnum statusVoucher, Integer quantity);
}
