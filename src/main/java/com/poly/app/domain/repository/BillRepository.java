package com.poly.app.domain.repository;

import com.poly.app.domain.admin.Statistical.Response.StatisticResponse;
import com.poly.app.domain.model.Bill;
import com.poly.app.infrastructure.constant.BillStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer>, JpaSpecificationExecutor<Bill> {

    Page<Bill> findByStatus(BillStatus status, Pageable pageable);


    Bill findByBillCode(String billCode);

    Boolean existsByBillCode(String billCode);

    @Query(value = "select b from Bill b where b.customer.id = :customerId order by b.createdAt desc")
    Page<Bill> findByCustomerId(Integer customerId,Pageable pageable);
    @Query("SELECT o.status, COUNT(o) FROM Bill o GROUP BY o.status")
    List<Object[]> countOrdersByStatus();

    @Query(value = """
               SELECT\s
                   all_statuses.status,
                   CASE\s
                       WHEN all_statuses.status = 'TONG_CONG'\s
                       THEN SUM(COUNT(b.id)) OVER ()  -- Tính tổng tất cả đơn hàng
                       ELSE COUNT(b.id)\s
                   END AS totalOrders
               FROM (
                   SELECT 'CHO_XAC_NHAN' AS status
                   UNION ALL SELECT 'DA_XAC_NHAN'
                   UNION ALL SELECT 'CHO_VAN_CHUYEN'
                   UNION ALL SELECT 'DANG_VAN_CHUYEN'
                   UNION ALL SELECT 'DA_THANH_TOAN'
                   UNION ALL SELECT 'DA_HOAN_THANH'
                   UNION ALL SELECT 'DA_HUY'
                   UNION ALL SELECT 'TRA_HANG'
                   UNION ALL SELECT 'TONG_CONG'  -- Dòng tổng
               ) AS all_statuses
               LEFT JOIN bill b ON b.status = all_statuses.status
               GROUP BY all_statuses.status
               ORDER BY FIELD(all_statuses.status,
                              'CHO_XAC_NHAN', 'DA_XAC_NHAN', 'CHO_VAN_CHUYEN',
                              'DANG_VAN_CHUYEN', 'DA_THANH_TOAN', 'DA_HOAN_THANH',
                              'DA_HUY', 'TRA_HANG', 'TONG_CONG');
               
            """, nativeQuery = true)
    List<Object[]> getFullBillStatusSummary();




    // Thống kê theo ngày
    @Query(value = """
    SELECT 
        DATE_FORMAT(FROM_UNIXTIME(b.updated_at / 1000), '%d/%m') AS label,
        COUNT(DISTINCT b.id) AS orders,
        SUM(bd.quantity) AS quantity,
        SUM(b.money_after) AS totalMoney
    FROM bill b
    JOIN bill_detail bd ON b.id = bd.bill_id
    WHERE b.status = 'DA_HOAN_THANH'
      AND DATE(FROM_UNIXTIME(b.updated_at / 1000)) BETWEEN :startDate AND :endDate
    GROUP BY DATE(FROM_UNIXTIME(b.updated_at / 1000))
    ORDER BY DATE(FROM_UNIXTIME(b.updated_at / 1000))
""", nativeQuery = true)
    List<StatisticResponse> getStatisticsByDay(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Thống kê theo tuần
    @Query(value = """
    SELECT 
        CONCAT(YEAR(FROM_UNIXTIME(b.updated_at / 1000)), '-Tuần ', WEEK(FROM_UNIXTIME(b.updated_at / 1000))) AS label,
        COUNT(DISTINCT b.id) AS orders,
        SUM(bd.quantity) AS quantity,
        SUM(b.money_after) AS totalMoney
    FROM bill b
    JOIN bill_detail bd ON b.id = bd.bill_id
    WHERE b.status = 'DA_HOAN_THANH'
      AND DATE(FROM_UNIXTIME(b.updated_at / 1000)) BETWEEN :startDate AND :endDate
    GROUP BY YEAR(FROM_UNIXTIME(b.updated_at / 1000)), WEEK(FROM_UNIXTIME(b.updated_at / 1000))
    ORDER BY YEAR(FROM_UNIXTIME(b.updated_at / 1000)), WEEK(FROM_UNIXTIME(b.updated_at / 1000))
""", nativeQuery = true)
    List<StatisticResponse> getStatisticsByWeek(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Thống kê theo năm
    @Query(value = """
    SELECT 
        CAST(YEAR(FROM_UNIXTIME(b.updated_at / 1000)) AS CHAR) AS label,
        COUNT(DISTINCT b.id) AS orders,
        SUM(bd.quantity) AS quantity,
        SUM(b.money_after) AS totalMoney
    FROM bill b
    JOIN bill_detail bd ON b.id = bd.bill_id
    WHERE b.status = 'DA_HOAN_THANH'
      AND DATE(FROM_UNIXTIME(b.updated_at / 1000)) BETWEEN :startDate AND :endDate
    GROUP BY YEAR(FROM_UNIXTIME(b.updated_at / 1000))
    ORDER BY YEAR(FROM_UNIXTIME(b.updated_at / 1000))
""", nativeQuery = true)
    List<StatisticResponse> getStatisticsByYear(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


}
