package com.poly.app.domain.admin.Statistical.Repository;

import com.poly.app.domain.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MinProductRepository extends JpaRepository<ProductDetail, Integer> {
    @Query(value = """
        SELECT 
            p.id AS productId,
            p.product_name AS productName,
            t.type_name AS typeName,
            c.color_name AS colorName,
            s.size_name AS sizeName,
            sl.sole_name AS soleName,
            g.gender_name AS genderName,
            pd.price AS price,
            pd.quantity AS quantity
        FROM product_detail pd
        JOIN product p ON pd.product_id = p.id
        LEFT JOIN type t ON pd.type_id = t.id
        LEFT JOIN color c ON pd.color_id = c.id
        LEFT JOIN size s ON pd.size_id = s.id
        LEFT JOIN sole sl ON pd.sole_id = sl.id
        LEFT JOIN gender g ON pd.gender_id = g.id
        WHERE pd.quantity <= :quantity
            ORDER BY pd.quantity ASC
        
    """, nativeQuery = true)
    List<Object[]> findProductsWithQuantityLessThan(@Param("quantity") int quantity);
}
