package com.poly.app.domain.repository;

import com.poly.app.domain.admin.product.response.img.ImgResponse;
import com.poly.app.domain.model.Image;
import com.poly.app.domain.model.ProductDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    Image getImagesByProductDetail(ProductDetail productDetail);
    Image findDistinctFirstByProductDetail(ProductDetail productDetail);

    @Query("SELECT new com.poly.app.domain.admin.product.response.img.ImgResponse(i.id,i.url,i.publicId) " +
           "FROM Image i WHERE i.productDetail.id = :productDetailId")
    List<ImgResponse> findByProductDetailId(@Param("productDetailId") Integer productDetailId);
    @Query("""
    SELECT new com.poly.app.domain.admin.product.response.img.ImgResponse(i.id, i.url, i.publicId) 
    FROM Image i 
    WHERE i.productDetail.product.id = :productId
    """)
    List<ImgResponse> findByProductId(@Param("productId") Integer productId);

    List<Image> getImageByPublicId(String publicId);

    // Xóa ảnh của ProductDetail theo productDetailId
    @Transactional
    void deleteByProductDetailId(int productDetailId);

    // Tìm tất cả ảnh của ProductDetail theo productDetailId
    List<ImgResponse> findByProductDetailId(int productDetailId);


    // kiemr tra xem public id có mấy màu sử dụng để quyết định có xóa trên cloud hay không
    @Query(value = "SELECT COUNT(DISTINCT pd.color_id) " +
                   "FROM image i " +
                   "JOIN product_detail pd ON i.product_detail_id = pd.id " +
                   "WHERE i.public_id = :publicId", nativeQuery = true)
    int countDistinctColorsByPublicId(@Param("publicId") String publicId);

    // lây mảng publicid thỏa mãn của 1 sản phẩm và có cùng màu khi sửa
    @Query(value = "SELECT i.* " +
                   "FROM image i " +
                   "JOIN product_detail pd ON i.product_detail_id = pd.id " +
                   "WHERE pd.product_id = :productId AND pd.color_id = :colorId",
            nativeQuery = true)
    List<Image> findPublicIdsByProductIdAndColorId(@Param("productId") Integer productId,
                                                    @Param("colorId") Integer colorId);


    List<Image> findByProductDetail_Id(Integer productDetailId);
}
