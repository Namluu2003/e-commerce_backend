package com.poly.app.domain.admin.Statistical.Repository;

import com.poly.app.domain.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrowthRateRepository extends JpaRepository<Bill, Integer> {
    //tốc độ thoe năm
    @Query(value = """
    WITH revenue_data AS (
        SELECT 
            YEAR(ship_date) AS year,
            COALESCE(SUM(total_money), 0) AS revenue
        FROM bill
                WHERE status = 'DA_HOAN_THANH'
        GROUP BY YEAR(ship_date)
    )
    SELECT 
        rd.year AS year,
        rd.revenue AS revenue,
        COALESCE(ld.revenue, 0) AS last_year_revenue,
        rd.revenue - COALESCE(ld.revenue, 0) AS difference,
        CASE 
            WHEN COALESCE(ld.revenue, 0) = 0 THEN '100%'
            ELSE CONCAT(ROUND(((rd.revenue - ld.revenue) / NULLIF(ld.revenue, 0)) * 100, 2), '%')
        END AS percentage_change
    FROM revenue_data rd
    LEFT JOIN revenue_data ld ON rd.year = ld.year + 1
    ORDER BY rd.year DESC
    LIMIT 1;
    """, nativeQuery = true)
    List<Object[]> getGroethRateYear();
    //Tốc độ theo tháng
    @Query(value = """
    WITH revenue_data AS (
        SELECT 
            YEAR(ship_date) AS year,
            MONTH(ship_date) AS month,
            COALESCE(SUM(total_money), 0) AS revenue
        FROM bill
                WHERE status = 'DA_HOAN_THANH'
        GROUP BY YEAR(ship_date), MONTH(ship_date)
    )
    SELECT 
        rd.year,
        rd.month,
        rd.revenue,
        COALESCE(ld.revenue, 0) AS last_month_revenue,
        rd.revenue - COALESCE(ld.revenue, 0) AS difference,
        CASE 
            WHEN COALESCE(ld.revenue, 0) = 0 THEN '100%'
            ELSE CONCAT(ROUND(((rd.revenue - ld.revenue) / ld.revenue) * 100, 2), '%')
        END AS percentage_change
    FROM revenue_data rd
    LEFT JOIN revenue_data ld 
        ON (rd.year = ld.year AND rd.month = ld.month + 1)  -- So sánh với tháng trước cùng năm
        OR (rd.year = ld.year + 1 AND rd.month = 1 AND ld.month = 12) -- So sánh tháng 1 với tháng 12 năm trước
    ORDER BY rd.year DESC, rd.month DESC
    LIMIT 1;
    """, nativeQuery = true)
    List<Object[]> getGrowthRateMonth();




}
