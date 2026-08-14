package com.poly.app.domain.admin.Statistical.Repository;

import com.poly.app.domain.admin.Statistical.Response.SumTotal;
import com.poly.app.domain.admin.Statistical.Service.BestSaleDTO;
import com.poly.app.domain.model.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticalRepository extends JpaRepository<Bill, Integer> {
    @Query(value = "SELECT COALESCE(SUM(total_money), 0 ) as total_money FROM bill"
            , nativeQuery = true)
    SumTotal SumBill();

    //ngày
    @Query(value = """
                SELECT 
                    DATE(FROM_UNIXTIME(b.created_at / 1000)) AS reportDate,
                   SUM(CASE WHEN b.status = 'DA_HOAN_THANH' THEN b.total_money ELSE 0 END) AS totalRevenue,
                 COUNT(DISTINCT b.id) AS totalOrders, 
             COUNT(DISTINCT CASE WHEN b.status = 'DA_HOAN_THANH' THEN b.id ELSE NULL END) AS successfullOrders,
                    SUM(CASE WHEN b.status = 'DA_HUY' THEN 1 ELSE 0 END) AS cancelledOrders,
            SUM(CASE WHEN b.status = 'DA_HOAN_THANH' THEN bd.quantity ELSE 0 END) AS totalProductsSold
                                             FROM bill b
                LEFT JOIN bill_detail bd ON b.id = bd.bill_id
                WHERE DATE(FROM_UNIXTIME(b.created_at / 1000)) = CURDATE()
                GROUP BY reportDate
                ORDER BY reportDate DESC
            """, nativeQuery = true)
    List<Object[]> getSumDay();


    // Tuần
    @Query(value = """
                SELECT 
                    CAST(YEARWEEK(FROM_UNIXTIME(b.created_at / 1000)) AS CHAR) AS reportTime,
                   SUM(CASE WHEN b.status = 'DA_HOAN_THANH' THEN b.total_money ELSE 0 END) AS totalRevenue,
                    COUNT(DISTINCT b.id) AS totalOrders, 
                    COUNT(DISTINCT CASE WHEN b.status = 'DA_HOAN_THANH' THEN b.id ELSE NULL END) AS successfullOrders,
                    SUM(CASE WHEN b.status = 'DA_HUY' THEN 1 ELSE 0 END) AS cancelledOrders,
            SUM(CASE WHEN b.status = 'DA_HOAN_THANH' THEN bd.quantity ELSE 0 END) AS totalProductsSold
                FROM bill b
                LEFT JOIN bill_detail bd ON b.id = bd.bill_id
                WHERE YEARWEEK(FROM_UNIXTIME(b.created_at / 1000)) = YEARWEEK(CURDATE())
                GROUP BY reportTime
                ORDER BY reportTime DESC
            """, nativeQuery = true)
    List<Object[]> getSumWeek();


    // Tháng
    @Query(value = """
                SELECT 
                    DATE_FORMAT(FROM_UNIXTIME(b.created_at / 1000), '%Y-%m') AS reportTime,
                   SUM(CASE WHEN b.status = 'DA_HOAN_THANH' THEN b.total_money ELSE 0 END) AS totalRevenue,
                 COUNT(DISTINCT b.id) AS totalOrders,
               COUNT(DISTINCT CASE WHEN b.status = 'DA_HOAN_THANH' THEN b.id ELSE NULL END) AS successfullOrders,
                    SUM(CASE WHEN b.status = 'DA_HUY' THEN 1 ELSE 0 END) AS cancelledOrders,
            SUM(CASE WHEN b.status = 'DA_HOAN_THANH' THEN bd.quantity ELSE 0 END) AS totalProductsSold
                                                              FROM bill b
                LEFT JOIN bill_detail bd ON b.id = bd.bill_id
                WHERE DATE_FORMAT(FROM_UNIXTIME(b.created_at / 1000), '%Y-%m') = DATE_FORMAT(CURDATE(), '%Y-%m')
                GROUP BY reportTime
                ORDER BY reportTime DESC
            """, nativeQuery = true)
    List<Object[]> getSumMonth();


    // Năm
    @Query(value = """
                SELECT 
                    CAST(YEAR(FROM_UNIXTIME(b.created_at / 1000)) AS CHAR) AS reportTime,
                   SUM(CASE WHEN b.status = 'DA_HOAN_THANH' THEN b.total_money ELSE 0 END) AS totalRevenue,
                    COUNT(DISTINCT b.id) AS totalOrders,
                 COUNT(DISTINCT CASE WHEN b.status = 'DA_HOAN_THANH' THEN b.id ELSE NULL END) AS successfullOrders,
                    SUM(CASE WHEN b.status = 'DA_HUY' THEN 1 ELSE 0 END) AS cancelledOrders,
                    SUM(CASE WHEN b.status = 'DA_HOAN_THANH' THEN bd.quantity ELSE 0 END) AS totalProductsSold
                    FROM bill b
                LEFT JOIN bill_detail bd ON b.id = bd.bill_id
                WHERE YEAR(FROM_UNIXTIME(b.created_at / 1000)) = YEAR(CURDATE())
                GROUP BY reportTime
                ORDER BY reportTime DESC
            """, nativeQuery = true)
    List<Object[]> getSumYear();


    // Tuỳ chỉnh ngày
    @Query(value = """
                SELECT 
                    CONCAT(:startDate, ' to ', :endDate) AS reportTime,
                   COALESCE(SUM(CASE WHEN b.status = 'DA_HOAN_THANH' THEN b.total_money ELSE 0 END), 0) AS totalRevenue,
                                                              COALESCE(COUNT(CASE WHEN b.status = 'DA_HOAN_THANH' THEN b.id ELSE NULL END), 0) AS totalOrders,  
                    COALESCE(SUM(CASE WHEN b.status = 'DA_HOAN_THANH' THEN 1 ELSE 0 END), 0) AS successfullOrders,
                    COALESCE(SUM(CASE WHEN b.status = 'DA_HUY' THEN 1 ELSE 0 END), 0) AS cancelledOrders,
                   COALESCE(SUM(CASE WHEN b.status = 'DA_HOAN_THANH' THEN bd.quantity ELSE 0 END), 0) AS totalProductsSold      
                FROM bill b
                LEFT JOIN bill_detail bd ON b.id = bd.bill_id
                WHERE DATE(FROM_UNIXTIME(b.created_at / 1000)) BETWEEN :startDate AND :endDate
            """, nativeQuery = true)
    List<Object[]> getSumByCustomDate(@Param("startDate") String startDate, @Param("endDate") String endDate);


    //lấy tên,size,màu,số lượng sản phẩm bán chạy nhất
    //lấy tổng
    @Query(value = "SELECT p.product_name, c.color_name, s.size_name, SUM(bd.quantity) AS totalQuantitySold " +
            "FROM bill_detail bd " +
            "JOIN product_detail pd ON bd.product_detail_id = pd.id " +
            "JOIN product p ON pd.product_id = p.id " +
            "JOIN color c ON pd.color_id = c.id " +
            "JOIN size s ON pd.size_id = s.id " +
            "GROUP BY p.product_name, c.color_name, s.size_name " +
            "ORDER BY totalQuantitySold DESC " +
            "LIMIT :offset, :limit",
            nativeQuery = true)
    List<Object[]> findBestSellingProduct(@Param("offset") int offset, @Param("limit") int limit);

    //ngày

    //tuần

    // tháng

    //năm
    @Query(value = "SELECT p.product_name, b.brand_name, t.type_name, c.color_name, " +
            "m.material_name, s.size_name, sole.sole_name, g.gender_name, " +
            "SUM(bd.quantity) AS total_quantity_sold, pd.price " +
            "FROM bill_detail bd " +
            "JOIN product_detail pd ON bd.product_detail_id = pd.id " +
            "JOIN product p ON pd.product_id = p.id " +
            "JOIN brand b ON pd.brand_id = b.id " +
            "JOIN type t ON pd.type_id = t.id " +
            "JOIN color c ON pd.color_id = c.id " +
            "JOIN material m ON pd.material_id = m.id " +
            "JOIN size s ON pd.size_id = s.id " +
            "JOIN sole ON pd.sole_id = sole.id " +
            "JOIN gender g ON pd.gender_id = g.id " +
            "JOIN bill bl ON bd.bill_id = bl.id " +
            "WHERE YEAR(FROM_UNIXTIME(bl.created_at / 1000)) = YEAR(CURDATE()) " +
            "GROUP BY p.product_name, b.brand_name, t.type_name, c.color_name, m.material_name, " +
            "s.size_name, sole.sole_name, g.gender_name, pd.price " +
            "ORDER BY total_quantity_sold DESC " +
            "LIMIT 1", nativeQuery = true)
    BestSaleDTO findTopSellingProductThisYear();

    //tùy chỉnh
}

