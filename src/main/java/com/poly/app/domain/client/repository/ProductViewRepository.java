package com.poly.app.domain.client.repository;


import com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse;
import com.poly.app.domain.client.response.ProductDetailDiscountDTO;
import com.poly.app.domain.client.response.ProductViewResponse;
import com.poly.app.domain.client.response.PromotionResponse;
import com.poly.app.domain.model.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductViewRepository extends JpaRepository<ProductDetail, Integer> {
    //    đây là repo của client
//    hiển thị các sản phẩm đang đưuọc giảm giá
    @Query(
            value = "SELECT " +
                    "p.id AS product_id, " +
                    "CONCAT(p.product_name, ' [', c.color_name, '-', g.gender_name, ']') AS product_name, " +
                    "MAX(pd.id) AS product_detail_id, " +
                    "(CONCAT(MIN(pd.price),' - ',MAX(pd.price))) AS price, " +
                    "SUM(pd.sold) AS sold, " +
                    "MAX(pd.color_id) AS color_id," +
                    "MAX(pd.size_id) AS size_id," +
                    "MAX(pd.gender_id) AS genderId, " +
                    "MAX(pd.material_id) AS materialId, " +
                    "MAX(pd.sole_id) AS soleId, " +
                    "MAX(pd.tag) AS tag, " +
                    "COALESCE((" +
                    "SELECT i.url " +
                    "FROM image i " +
                    "WHERE i.product_detail_id = MAX(pd.id) " +
                    "ORDER BY i.is_default DESC " +
                    "LIMIT 1" +
                    "), '') AS image_url, " +
                    "p.views,\n" +
                    "COALESCE(MAX(pr.promotion_name), 'Không có khuyến mãi') AS promotion_name, " +
                    "MAX(pd.created_at) AS created_at " +
                    "FROM product p " +
                    "JOIN product_detail pd ON p.id = pd.product_id " +
                    "LEFT JOIN color c ON pd.color_id = c.id " +
                    "LEFT JOIN gender g ON pd.gender_id = g.id " +
                    "LEFT JOIN product_promotion prd ON pd.id = prd.product_detail_id " +
                    "LEFT JOIN promotion pr ON prd.promotion_id = pr.id " +
                    "WHERE p.status = 0 " +
                    "AND pd.status = 0 " +
                    "AND pr.id IS NOT NULL " +
                    "AND NOW() BETWEEN pr.start_date AND pr.end_date " +
                    "GROUP BY p.id, p.product_name, c.color_name, g.gender_name " +
                    "ORDER BY created_at DESC",

            countQuery = "SELECT COUNT(*) FROM (" +
                         "SELECT p.id " +
                         "FROM product p " +
                         "JOIN product_detail pd ON p.id = pd.product_id " +
                         "LEFT JOIN color c ON pd.color_id = c.id " +
                         "LEFT JOIN gender g ON pd.gender_id = g.id " +
                         "LEFT JOIN product_promotion prd ON pd.id = prd.product_detail_id " +
                         "LEFT JOIN promotion pr ON prd.promotion_id = pr.id " +
                         "WHERE p.status = 0 " +
                         "AND pd.status = 0 " +
                         "AND pr.id IS NOT NULL " +
                         "AND NOW() BETWEEN pr.start_date AND pr.end_date " +
                         "GROUP BY p.id, p.product_name, c.color_name, g.gender_name" +
                         ") AS tmp",
            nativeQuery = true
    )
    Page<ProductViewResponse> getAllProductHadPromotion(Pageable pageable);

//    lấy cacs sản phẩm có lượt bán nhiều nhất

    @Query(
            value = "SELECT " +
                    "p.id AS product_id, " +
                    "CONCAT(p.product_name, ' [', c.color_name, '-', g.gender_name, ']') AS product_name, " +
                    "MAX(pd.id) AS product_detail_id, " +
                    "CONCAT(MIN(pd.price), ' - ', MAX(pd.price)) AS price, " +
                    "SUM(pd.sold) AS sold, " +
                    "MAX(pd.color_id) AS colorId, " +
                    "MAX(pd.size_id) AS sizeId, " +
                    "MAX(pd.gender_id) AS genderId, " +
                    "MAX(pd.material_id) AS materialId, " +
                    "MAX(pd.sole_id) AS soleId, " +

                    "MAX(pd.tag) AS tag, " +
                    "COALESCE(( " +
                    "    SELECT i.url " +
                    "    FROM image i " +
                    "    WHERE i.product_detail_id = MAX(pd.id) " +
                    "    ORDER BY i.is_default DESC " +
                    "    LIMIT 1 " +
                    "), '') AS image_url, " +
                    "p.views " +
                    "FROM product p " +
                    "JOIN product_detail pd ON p.id = pd.product_id " +
                    "LEFT JOIN color c ON pd.color_id = c.id " +
                    "LEFT JOIN gender g ON pd.gender_id = g.id " +
                    "WHERE p.status = 0 AND pd.status = 0 " +
                    "AND FROM_UNIXTIME(pd.updated_at / 1000) BETWEEN DATE_SUB(NOW(), INTERVAL 1 MONTH) AND NOW() " +
                    "GROUP BY p.id, p.product_name, c.color_name, g.gender_name " +
                    "ORDER BY sold DESC",
            countQuery = "SELECT COUNT(*) FROM ( " +
                         "SELECT p.id " +
                         "FROM product p " +
                         "JOIN product_detail pd ON p.id = pd.product_id " +
                         "LEFT JOIN color c ON pd.color_id = c.id " +
                         "LEFT JOIN gender g ON pd.gender_id = g.id " +
                         "WHERE p.status = 0 AND pd.status = 0 " +
                         "AND FROM_UNIXTIME(pd.updated_at / 1000) BETWEEN DATE_SUB(NOW(), INTERVAL 1 MONTH) AND NOW() " +
                         "GROUP BY p.id, p.product_name, c.color_name, g.gender_name " +
                         ") AS tmp",
            nativeQuery = true
    )
    Page<ProductViewResponse> getAllProductHadSoleDesc(Pageable pageable);


    //lấy các sản phẩm mới theo ngày tạo
    @Query(
            value = "SELECT " +
                    "p.id AS product_id, " +
                    "CONCAT(p.product_name, ' [', c.color_name, '-', g.gender_name, ']') AS product_name, " +
                    "MAX(pd.id) AS product_detail_id, " +
                    "MIN(pd.price) AS price, " +
                    "MAX(pd.sold) AS sold, " +
                    "MAX(pd.color_id) AS color_id," +
                    "MAX(pd.size_id) AS size_id," +
                    "MAX(pd.tag) AS tag, " +
                    "COALESCE((" +
                    "SELECT i.url " +
                    "FROM image i " +
                    "WHERE i.product_detail_id = MAX(pd.id) " +
                    "ORDER BY i.is_default DESC " +
                    "LIMIT 1" +
                    "), '') AS image_url, " +
                    "p.views,\n" +

                    "COALESCE(MAX(pr.promotion_name), 'Không có khuyến mãi') AS promotion_name, " +
                    "COALESCE(MAX(pr.discount_value), 0) AS discount_value, " +
                    "COALESCE(MAX(pr.promotion_type), 'none') AS promotion_type, " +
                    "MAX(pd.created_at) AS created_at " +
                    "FROM product p " +
                    "JOIN product_detail pd ON p.id = pd.product_id " +
                    "LEFT JOIN color c ON pd.color_id = c.id " +
                    "LEFT JOIN gender g ON pd.gender_id = g.id " +
                    "LEFT JOIN product_promotion prd ON pd.id = prd.product_detail_id " +
                    "LEFT JOIN promotion pr ON prd.promotion_id = pr.id " +
                    "WHERE p.status = 0 AND pd.status = 0 " +
                    "GROUP BY p.id, p.product_name, c.color_name, g.gender_name " +
                    "ORDER BY created_at DESC ",
            countQuery = "SELECT COUNT(*) FROM ( " +
                         "SELECT p.id " +
                         "FROM product p " +
                         "JOIN product_detail pd ON p.id = pd.product_id " +
                         "LEFT JOIN color c ON pd.color_id = c.id " +
                         "LEFT JOIN gender g ON pd.gender_id = g.id " +
                         "LEFT JOIN product_promotion prd ON pd.id = prd.product_detail_id " +
                         "LEFT JOIN promotion pr ON prd.promotion_id = pr.id " +
                         "WHERE p.status = 0 AND pd.status = 0 " +
                         "GROUP BY p.id, p.product_name, c.color_name, g.gender_name " +
                         ") AS tmp",
            nativeQuery = true
    )
    Page<ProductViewResponse> getAllProductHadCreatedAtDesc(Pageable pageable);

    //sắp xếp theo lượt vào xem của sản phẩm
    @Query(
            value = "SELECT \n" +
                    "    p.id AS product_id,\n" +
                    "    -- Gộp tên sản phẩm với màu, giới tính\n" +
                    "    CONCAT(p.product_name, ' [', c.color_name, '-', g.gender_name, ']') AS product_name,\n" +
                    "    \n" +
                    "    MAX(pd.id) AS product_detail_id,\n" +
                    "    MIN(pd.price) AS price,\n" +
                    "    MAX(pd.sold) AS sold,  \n" +
                    "MAX(pd.color_id) AS color_id," +
                    "MAX(pd.size_id) AS size_id," +
                    "    MAX(pd.tag) AS tag,    \n" +
                    "\n" +
                    "    -- Lấy ảnh theo màu\n" +
                    "    COALESCE((\n" +
                    "        SELECT i.url \n" +
                    "        FROM image i \n" +
                    "        WHERE i.product_detail_id = MAX(pd.id)  \n" +
                    "        ORDER BY i.is_default DESC\n" +
                    "        LIMIT 1\n" +
                    "    ), '') AS image_url,\n" +
                    "p.views,\n" +
                    "    COALESCE(MAX(pr.promotion_name), 'Không có khuyến mãi') AS promotion_name,\n" +
                    "    COALESCE(MAX(pr.discount_value), 0) AS discount_value,\n" +
                    "    COALESCE(MAX(pr.promotion_type), 'none') AS promotion_type,\n" +
                    "MAX(pd.created_at) AS created_at \n" +
                    "FROM product p\n" +
                    "JOIN product_detail pd ON p.id = pd.product_id\n" +
                    "LEFT JOIN color c ON pd.color_id = c.id  -- Lấy tên màu từ bảng color\n" +
                    "LEFT JOIN gender g ON pd.gender_id = g.id  -- Lấy tên giới tính\n" +
                    "LEFT JOIN product_promotion prd ON pd.id = prd.product_detail_id\n" +
                    "LEFT JOIN promotion pr ON prd.promotion_id = pr.id\n" +
                    "\n" +
                    "WHERE p.status = 0 AND pd.status = 0\n" +
                    "GROUP BY p.id, p.product_name, c.color_name, g.gender_name\n" +
                    "\n" +
                    "ORDER BY p.views DESC",
            countQuery = "SELECT COUNT(*) FROM ( " +
                         "SELECT p.id " +
                         "FROM product p " +
                         "JOIN product_detail pd ON p.id = pd.product_id " +
                         "LEFT JOIN color c ON pd.color_id = c.id " +
                         "LEFT JOIN gender g ON pd.gender_id = g.id " +
                         "LEFT JOIN product_promotion prd ON pd.id = prd.product_detail_id " +
                         "LEFT JOIN promotion pr ON prd.promotion_id = pr.id " +
                         "WHERE p.status = 0 AND pd.status = 0 " +
                         "GROUP BY p.id, p.product_name, c.color_name, g.gender_name " +
                         ") AS tmp",
            nativeQuery = true
    )
    Page<ProductViewResponse> getAllProductHadViewsDesc(Pageable pageable);

    @Query(value = "select  new com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse" +
                   "(pd.id,pd.code,pd.product.productName,pd.brand.brandName,pd.type.typeName,pd.color.colorName" +
                   ",pd.material.materialName,pd.size.sizeName,pd.sole.soleName,pd.gender.genderName,pd.quantity" +
                   ",pd.price,pd.weight,pd.descrition,pd.status,pd.updatedAt,pd.updatedBy) from ProductDetail pd  " +
                   "where pd.product.id = :productId and pd.color.id= :colorId and pd.size.id = :sizeId " +
                   "and pd.gender.id = :genderId and pd.material.id = :materialId and pd.sole.id = :soleId  ")
    List<ProductDetailResponse> findByProductAndColorAndSize(int productId, int colorId, int sizeId, int genderId, int materialId, int soleId);

    @Query(value = "SELECT NEW com.poly.app.domain.client.response.PromotionResponse" +
                   "(pd.promotion.id, pd.productDetail.id, pd.promotion.discountValue, pd.promotion.promotionName) " +
                   "FROM ProductPromotion pd " +
                   "WHERE pd.productDetail.id = :productDetailId " +
                   "AND :currentTime BETWEEN pd.promotion.startDate AND pd.promotion.endDate")
    List<PromotionResponse> findPromotionByProductDetailId(@Param("productDetailId") Integer productDetailId,@Param("currentTime") LocalDateTime timestamp);

//lọc

        @Query(value = "SELECT pd.product_id AS productId, " +
                       "CONCAT(p.product_name, ' [', c.color_name, '-', g.gender_name, ']') AS productName, " +
                       "MAX(pd.id) AS productDetailId, " +
                       "CONCAT(MIN(pd.price), ' - ', MAX(pd.price)) AS price, " +
                       "CONCAT(MIN(pr.discount_value), ' - ', MAX(pr.discount_value)) AS discountValue, " +
                       "SUM(pd.sold) AS sold, " +
                       "MAX(pd.created_At) as createdAt,"+
                       "MAX(pd.color_id) AS colorId, " +
                       "MAX(pd.gender_id) AS genderId, " +
                       "MAX(pd.size_id) AS sizeId, " +
                       "MAX(pd.material_id) AS materialId, " +
                       "MAX(pd.sole_id) AS soleId, " +
                       "MAX(pd.tag) AS tag, " +
                       "p.views,"+
                       "COALESCE((SELECT i.url " +
                       "          FROM image i " +
                       "          INNER JOIN product_detail pd_sub " +
                       "          ON i.product_detail_id = pd_sub.id " +
                       "          AND pd_sub.color_id = MAX(pd.color_id) " +
                       "          WHERE pd_sub.product_id = pd.product_id " +
                       "          AND pd_sub.status = 0 " +
                       "          ORDER BY i.is_default DESC " +
                       "          LIMIT 1), '') AS imageUrl " +
                       "FROM product_detail pd " +
                       "LEFT JOIN product p ON pd.product_id = p.id " +
                       "LEFT JOIN color c ON pd.color_id = c.id " +
                       "LEFT JOIN gender g ON pd.gender_id = g.id " +
                       "LEFT JOIN type t ON pd.type_id = t.id " +
                       "LEFT JOIN material m ON pd.material_id = m.id " +
                       "LEFT JOIN product_promotion prd ON pd.id = prd.product_detail_id " +
                       "LEFT JOIN promotion pr ON prd.promotion_id = pr.id " +
                       "WHERE pd.status = 0 " +
                       "AND (:productId IS NULL OR pd.product_id = :productId) " +
                       "AND (:brandId IS NULL OR pd.brand_id = :brandId) " +
                       "AND (:genderId IS NULL OR pd.gender_id = :genderId) " +
                       "AND (:typeId IS NULL OR pd.type_id = :typeId) " +
                       "AND (:colorId IS NULL OR pd.color_id = :colorId) " +
                       "AND (:materialId IS NULL OR pd.material_id = :materialId) " +
                       "AND (:minPrice IS NULL OR pd.price >= :minPrice) " +
                       "AND (:maxPrice IS NULL OR pd.price <= :maxPrice) " +
                       "GROUP BY pd.product_id, c.color_name, g.gender_name " +
                       "ORDER BY createdAt DESC",
                countQuery = "SELECT COUNT(DISTINCT CONCAT(pd.product_id, c.color_name, g.gender_name)) " +
                             "FROM product_detail pd " +
                             "LEFT JOIN product p ON pd.product_id = p.id " +
                             "LEFT JOIN color c ON pd.color_id = c.id " +
                             "LEFT JOIN gender g ON pd.gender_id = g.id " +
                             "LEFT JOIN type t ON pd.type_id = t.id " +
                             "LEFT JOIN material m ON pd.material_id = m.id " +
                             "LEFT JOIN product_promotion prd ON pd.id = prd.product_detail_id " +
                             "LEFT JOIN promotion pr ON prd.promotion_id = pr.id " +
                             "WHERE pd.status = 0 " +
                             "AND (:productId IS NULL OR pd.product_id = :productId) " +
                             "AND (:brandId IS NULL OR pd.brand_id = :brandId) " +

                             "AND (:genderId IS NULL OR pd.gender_id = :genderId) " +
                             "AND (:typeId IS NULL OR pd.type_id = :typeId) " +
                             "AND (:colorId IS NULL OR pd.color_id = :colorId) " +
                             "AND (:materialId IS NULL OR pd.material_id = :materialId) " +
                             "AND (:minPrice IS NULL OR pd.price >= :minPrice) " +
                             "AND (:maxPrice IS NULL OR pd.price <= :maxPrice)",
                nativeQuery = true)
        Page<ProductViewResponse> findFilteredProducts(
                @Param("productId") Long productId,
                @Param("brandId") Long brandId,
                @Param("genderId") Long genderId,
                @Param("typeId") Long typeId,
                @Param("colorId") Long colorId,
                @Param("materialId") Long materialId,
                @Param("minPrice") Double minPrice,
                @Param("maxPrice") Double maxPrice,
                Pageable pageable
        );
    @Query(value = "SELECT " +
                   "prd.id AS productId, " +
                   "pd.id AS productDetailId, " +
                   "pd.color_id AS colorId, " +
                   "pd.gender_id AS genderId, " +
                   "pd.price, " +
                   "MAX(p.discount_value) AS max, " +
                   "CONCAT(pd.price - (pd.price * MAX(p.discount_value) / 100)) AS giasaugiam " +
                   "FROM product_detail pd " +
                   "LEFT JOIN product_promotion pp ON pd.id = pp.product_detail_id " +
                   "LEFT JOIN promotion p ON pp.promotion_id = p.id " +
                   "LEFT JOIN product prd ON prd.id = pd.product_id " +
                   "WHERE pd.status = 0 " +
                   "AND prd.id = :productId " +
                   "AND pd.color_id = :colorId " +
                   "AND pd.gender_id = :genderId " +
                   "AND :currentTime BETWEEN p.start_date AND p.end_date " +
                   "GROUP BY pd.id, pd.price, pd.color_id, pd.gender_id",
            nativeQuery = true)
    List<Object[]> findDiscountedProductDetails(Integer productId, Integer colorId, Integer genderId, Timestamp currentTime);

    @Query(value = "SELECT \n" +
                   "  EXISTS (\n" +
                   "    SELECT 1\n" +
                   "    FROM bill b\n" +
                   "    JOIN bill_detail bd ON bd.bill_id = b.id\n" +
                   "    JOIN product_detail pd ON pd.id = bd.product_detail_id\n" +
                   "    JOIN product p ON p.id = pd.product_id\n" +
                   "    WHERE b.customer_id = :customerId AND p.id = :productId AND b.status ='DA_HOAN_THANH'\n" +
                   "  ) AS has_bought;",nativeQuery = true)
    Long hasBought(@Param("customerId") Integer customerId, @Param("productId") Integer productId);
}
