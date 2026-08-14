package com.poly.app.domain.repository;

import com.poly.app.domain.model.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPromotionRepository extends JpaRepository<ProductPromotion, Integer> {
    //       Tìm tất cả bản ghi ProductPromotion theo Promotion
    List<ProductPromotion> findProductPromotionByPromotion(Promotion promotion);
//     Xóa bản ghi ProductPromotion theo Product, ProductDetail, và Promotion
    void deleteByProductDetailAndPromotion( ProductDetail productDetail, Promotion promotion);
//    void deleteByProductAndProductDetailAndPromotion(Product product, ProductDetail productDetail, Promotion promotion);
//      Xóa tất cả ProductPromotion theo promotionId (sửa lỗi sai tên hàm)

@Modifying
@Transactional
@Query("DELETE FROM ProductPromotion pp WHERE pp.promotion.id = :promotionId")
void deleteAllByPromotionId(@Param("promotionId") int promotionId);

//      Tìm danh sách ProductPromotion theo Product ID
//    @Query("SELECT pp FROM ProductPromotion pp WHERE pp.product.id = :productId")
//    List<ProductPromotion> findProductPromotionByProductId(@Param("productId") Integer productId);
}
