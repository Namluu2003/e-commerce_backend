package com.poly.app.domain.admin.Statistical.Repository;

import com.poly.app.domain.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrowthRateProductRepository extends JpaRepository<Bill, Integer> {

    // Tốc độ tăng trưởng sản phẩm theo tháng
    @Query(value = """
                WITH sales_data AS (
                    SELECT 
                        YEAR(FROM_UNIXTIME(b.created_at / 1000)) AS year,
                        MONTH(FROM_UNIXTIME(b.created_at / 1000)) AS month,
                        SUM(bd.quantity) AS totalProductsSold
                    FROM bill b
                    LEFT JOIN bill_detail bd ON b.id = bd.bill_id
                    WHERE b.status = 'DA_HOAN_THANH'
                    GROUP BY year, month
                )
                SELECT 
                    CM.year AS year,
                    CM.month AS month,
                    IFNULL(CM.totalProductsSold, 0) AS total_sold_this_month,
                    IFNULL(LM.totalProductsSold, 0) AS last_month_sold,
                    (IFNULL(CM.totalProductsSold, 0) - IFNULL(LM.totalProductsSold, 0)) AS difference,
                    CASE 
                        WHEN LM.totalProductsSold IS NULL OR LM.totalProductsSold = 0 THEN '100%'
                        ELSE CONCAT(ROUND(((CM.totalProductsSold - LM.totalProductsSold) / LM.totalProductsSold) * 100), '%')
                    END AS percentageChange
                FROM sales_data CM
                LEFT JOIN sales_data LM
                    ON CM.year = LM.year AND CM.month = LM.month + 1
                ORDER BY CM.year, CM.month;
            """, nativeQuery = true)
    List<Object[]> getSSProductMonth();


    // Tốc độ tăng trưởng sản phẩm theo năm
    @Query(value = """
            WITH sales_data AS (
                SELECT
                    YEAR(FROM_UNIXTIME(b.created_at / 1000)) AS year,
                    SUM(bd.quantity) AS totalProductsSold
                FROM bill b
                LEFT JOIN bill_detail bd ON b.id = bd.bill_id
                WHERE b.status = 'DA_HOAN_THANH'
                GROUP BY year
            )
            SELECT
                CY.year AS year,
                IFNULL(CY.totalProductsSold, 0) AS total_sold_this_year,
                IFNULL(LY.totalProductsSold, 0) AS last_year_sold,
                (IFNULL(CY.totalProductsSold, 0) - IFNULL(LY.totalProductsSold, 0)) AS difference,
                CASE
                    WHEN LY.totalProductsSold IS NULL THEN '100%'  -- Nếu không có dữ liệu năm trước, hiển thị "100%"
                    ELSE CONCAT(ROUND(((CY.totalProductsSold - LY.totalProductsSold) / LY.totalProductsSold) * 100), '%')
                END AS yearPercentageChange
            FROM sales_data CY
            LEFT JOIN sales_data LY
                ON CY.year = LY.year + 1
            ORDER BY CY.year;

                                         

                                             
                            """, nativeQuery = true)
    List<Object[]> getSSProductYear();


}
