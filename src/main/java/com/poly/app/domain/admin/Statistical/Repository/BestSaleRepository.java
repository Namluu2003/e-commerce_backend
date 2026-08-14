package com.poly.app.domain.admin.Statistical.Repository;

import com.poly.app.domain.admin.Statistical.Service.BestSaleDTO;
import com.poly.app.domain.model.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BestSaleRepository extends JpaRepository<BillDetail,Integer> {
    // Lấy top 5 sản phẩm bán chạy nhất hôm nay
    @Query(value = """
        SELECT p.product_name, b.brand_name, t.type_name, c.color_name, 
               m.material_name, s.size_name, sole.sole_name, g.gender_name, 
               SUM(bd.quantity) AS total_quantity_sold, pd.price 
        FROM bill_detail bd
        JOIN product_detail pd ON bd.product_detail_id = pd.id
        JOIN product p ON pd.product_id = p.id
        JOIN brand b ON pd.brand_id = b.id
        JOIN type t ON pd.type_id = t.id
        JOIN color c ON pd.color_id = c.id
        JOIN material m ON pd.material_id = m.id
        JOIN size s ON pd.size_id = s.id
        JOIN sole ON pd.sole_id = sole.id
        JOIN gender g ON pd.gender_id = g.id
        JOIN bill bl ON bd.bill_id = bl.id
        WHERE DATE(FROM_UNIXTIME(bl.created_at / 1000)) = CURDATE()
                  AND bl.status = 'DA_HOAN_THANH'
        
        GROUP BY p.product_name, b.brand_name, t.type_name, c.color_name, m.material_name, 
                 s.size_name, sole.sole_name, g.gender_name, pd.price 
        ORDER BY total_quantity_sold DESC 
        LIMIT 5
        """, nativeQuery = true)
 List<Object[]> findTop5SellingProductsToday();

    // Lấy top 5 sản phẩm bán chạy nhất tuần này
    @Query(value = """
        SELECT p.product_name, b.brand_name, t.type_name, c.color_name, 
               m.material_name, s.size_name, sole.sole_name, g.gender_name, 
               SUM(bd.quantity) AS total_quantity_sold, pd.price 
        FROM bill_detail bd
        JOIN product_detail pd ON bd.product_detail_id = pd.id
        JOIN product p ON pd.product_id = p.id
        JOIN brand b ON pd.brand_id = b.id
        JOIN type t ON pd.type_id = t.id
        JOIN color c ON pd.color_id = c.id
        JOIN material m ON pd.material_id = m.id
        JOIN size s ON pd.size_id = s.id
        JOIN sole ON pd.sole_id = sole.id
        JOIN gender g ON pd.gender_id = g.id
        JOIN bill bl ON bd.bill_id = bl.id
        WHERE YEARWEEK(FROM_UNIXTIME(bl.created_at / 1000)) = YEARWEEK(CURDATE())
                  AND bl.status = 'DA_HOAN_THANH'
        
        GROUP BY p.product_name, b.brand_name, t.type_name, c.color_name, m.material_name, 
                 s.size_name, sole.sole_name, g.gender_name, pd.price 
        ORDER BY total_quantity_sold DESC 
        LIMIT 5
        """, nativeQuery = true)
 List<Object[]> findTop5SellingProductsThisWeek();

    // Lấy top 5 sản phẩm bán chạy nhất tháng này
    @Query(value = """
        SELECT p.product_name, b.brand_name, t.type_name, c.color_name, 
               m.material_name, s.size_name, sole.sole_name, g.gender_name, 
               SUM(bd.quantity) AS total_quantity_sold, pd.price 
        FROM bill_detail bd
        JOIN product_detail pd ON bd.product_detail_id = pd.id
        JOIN product p ON pd.product_id = p.id
        JOIN brand b ON pd.brand_id = b.id
        JOIN type t ON pd.type_id = t.id
        JOIN color c ON pd.color_id = c.id
        JOIN material m ON pd.material_id = m.id
        JOIN size s ON pd.size_id = s.id
        JOIN sole ON pd.sole_id = sole.id
        JOIN gender g ON pd.gender_id = g.id
        JOIN bill bl ON bd.bill_id = bl.id
        WHERE YEAR(FROM_UNIXTIME(bl.created_at / 1000)) = YEAR(CURDATE()) 
          AND MONTH(FROM_UNIXTIME(bl.created_at / 1000)) = MONTH(CURDATE())
                    AND bl.status = 'DA_HOAN_THANH'
          
        GROUP BY p.product_name, b.brand_name, t.type_name, c.color_name, m.material_name, 
                 s.size_name, sole.sole_name, g.gender_name, pd.price 
        ORDER BY total_quantity_sold DESC 
        LIMIT 5
        """, nativeQuery = true)
 List<Object[]> findTop5SellingProductsThisMonth();
    // ✅ Lấy top 5 sản phẩm bán chạy nhất năm nay
    @Query(value = """
        SELECT p.product_name, b.brand_name, t.type_name, c.color_name, 
               m.material_name, s.size_name, sole.sole_name, g.gender_name, 
               SUM(bd.quantity) AS total_quantity_sold, pd.price 
        FROM bill_detail bd
        JOIN product_detail pd ON bd.product_detail_id = pd.id
        JOIN product p ON pd.product_id = p.id
        JOIN brand b ON pd.brand_id = b.id
        JOIN type t ON pd.type_id = t.id
        JOIN color c ON pd.color_id = c.id
        JOIN material m ON pd.material_id = m.id
        JOIN size s ON pd.size_id = s.id
        JOIN sole ON pd.sole_id = sole.id
        JOIN gender g ON pd.gender_id = g.id
        JOIN bill bl ON bd.bill_id = bl.id
        WHERE YEAR(FROM_UNIXTIME(bl.created_at / 1000)) = YEAR(CURDATE()) 
                  AND bl.status = 'DA_HOAN_THANH'
        
        GROUP BY p.product_name, b.brand_name, t.type_name, c.color_name, m.material_name, 
                 s.size_name, sole.sole_name, g.gender_name, pd.price 
        ORDER BY total_quantity_sold DESC 
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> findTop5SellingProductsThisYear();

    // ✅ Lấy top 5 sản phẩm bán chạy theo khoảng ngày tùy chỉnh
    @Query(value = """
        SELECT p.product_name, b.brand_name, t.type_name, c.color_name, 
               m.material_name, s.size_name, sole.sole_name, g.gender_name, 
               SUM(bd.quantity) AS total_quantity_sold, pd.price 
        FROM bill_detail bd
        JOIN product_detail pd ON bd.product_detail_id = pd.id
        JOIN product p ON pd.product_id = p.id
        JOIN brand b ON pd.brand_id = b.id
        JOIN type t ON pd.type_id = t.id
        JOIN color c ON pd.color_id = c.id
        JOIN material m ON pd.material_id = m.id
        JOIN size s ON pd.size_id = s.id
        JOIN sole ON pd.sole_id = sole.id
        JOIN gender g ON pd.gender_id = g.id
        JOIN bill bl ON bd.bill_id = bl.id
        WHERE DATE(FROM_UNIXTIME(bl.created_at / 1000)) BETWEEN :startDate AND :endDate
                  AND bl.status = 'DA_HOAN_THANH'
        
        GROUP BY p.product_name, b.brand_name, t.type_name, c.color_name, m.material_name, 
                 s.size_name, sole.sole_name, g.gender_name, pd.price 
        ORDER BY total_quantity_sold DESC 
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> findTop5SellingProductsByCustomDateRange(@Param("startDate") String startDate,
                                                            @Param("endDate") String endDate);
}
