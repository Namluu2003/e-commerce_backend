package com.poly.app.domain.repository;

import com.poly.app.domain.admin.product.response.productdetail.ProductAtributeExistResponse;
import com.poly.app.domain.model.ProductDetail;
import com.poly.app.domain.admin.product.response.productdetail.FilterProductDetailResponse;
import com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse;
import com.poly.app.infrastructure.constant.Status;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {

    @Query(value = "select  new com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse" +
                   "(pd.id,pd.code,pd.product.productName,pd.brand.brandName,pd.type.typeName,pd.color.colorName" +
                   ",pd.material.materialName,pd.size.sizeName,pd.sole.soleName,pd.gender.genderName,pd.quantity" +
                   ",pd.price,pd.weight,pd.descrition,pd.status,pd.updatedAt,pd.updatedBy) from ProductDetail pd  order by pd.updatedAt desc ")
    Page<ProductDetailResponse> getAllProductDetailPage(Pageable pageable);

    @Query(value = "select  new com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse" +
                   "(pd.id,pd.code,pd.product.productName,pd.brand.brandName,pd.type.typeName,pd.color.colorName" +
                   ",pd.material.materialName,pd.size.sizeName,pd.sole.soleName,pd.gender.genderName,pd.quantity" +
                   ",pd.price,pd.weight,pd.descrition,pd.status,pd.updatedAt,pd.updatedBy) from ProductDetail pd order by pd.updatedAt desc ")
    List<ProductDetailResponse> getAllProductDetail();

    //ten

    @Query("SELECT new com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse" +
           "(pd.id, pd.code, pd.product.productName, pd.brand.brandName, pd.type.typeName, pd.color.colorName, " +
           " pd.material.materialName, pd.size.sizeName, pd.sole.soleName, pd.gender.genderName, pd.quantity, " +
           " pd.price, pd.weight, pd.descrition, pd.status, pd.updatedAt, pd.updatedBy) " +
           "FROM ProductDetail pd WHERE pd.product.productName = :productName ORDER BY pd.updatedAt DESC")
    List<ProductDetailResponse> getAllProductDetailByProductName(@Param("productName") String productName);

//        @Query("SELECT new com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse" +
//                "(pd.id, pd.code, pd.product.productName, pd.brand.brandName, pd.type.typeName, pd.color.colorName, " +
//                " pd.material.materialName, pd.size.sizeName, pd.sole.soleName, pd.gender.genderName, pd.quantity, " +
//                " pd.price, pd.weight, pd.descrition, pd.status, pd.updatedAt, pd.updatedBy) " +
//                "FROM ProductDetail pd ORDER BY pd.updatedAt DESC")
//        List<ProductDetailResponse> getAllProductDetails();


    List<ProductDetail> findProductDetailByProduct_Id(Integer productId);

    //    @Query(value = "CALL sp_FilterProductDetails(" +
//            ":productName, :brandName, :typeName, :colorName, :materialName," +
//            " :sizeName, :soleName, :genderName, :status, :sortByQuantity, :sortByPrice, :page, :size);", nativeQuery = true)
//    List<FilterProductDetailResponse> getFilterProductDetail(@Param("productName") String productName,
//                                                             @Param("brandName") String brandName,
//                                                             @Param("typeName") String typeName,
//                                                             @Param("colorName") String colorName,
//                                                             @Param("materialName") String materialName,
//                                                             @Param("sizeName") String sizeName,
//                                                             @Param("soleName") String soleName,
//                                                             @Param("genderName") String genderName,
//                                                             @Param("status") String status,
//                                                             @Param("sortByQuantity") String sortByQuantity,
//                                                             @Param("sortByPrice") String sortByPrice,
//                                                             @Param("page") int page,
//                                                             @Param("size") int size
//
//    );
    @Query(value =
            "SELECT \n" +
            "    pd.id,\n" +
            "    pd.code,\n" +
            "    p.product_name,\n" +
            "    b.brand_name,\n" +
            "    t.type_name,\n" +
            "    c.color_name,\n" +
            "    m.material_name,\n" +
            "    s.size_name,\n" +
            "    so.sole_name,\n" +
            "    g.gender_name,\n" +
            "    pd.quantity,\n" +
            "(select img.url from image img where product_detail_id = pd.id limit 1 )as image," +
            "    pd.price,\n" +
            "    pd.weight,\n" +
            "    pd.descrition,\n" +
            "    pd.status AS statusRoot,\n" +
            "    pd.updated_at AS updatedAt,\n" +
            "    pd.updated_by AS updatedBy\n" +
            "FROM \n" +
            "    product_detail pd\n" +
            "    JOIN product p ON pd.product_id = p.id\n" +
            "    JOIN brand b ON pd.brand_id = b.id\n" +
            "    JOIN type t ON pd.type_id = t.id\n" +
            "    JOIN color c ON pd.color_id = c.id\n" +
            "    JOIN material m ON pd.material_id = m.id\n" +
            "    JOIN size s ON pd.size_id = s.id\n" +
            "    JOIN sole so ON pd.sole_id = so.id\n" +
            "    JOIN gender g ON pd.gender_id = g.id\n" +
            "WHERE \n" +
            "    (:productName IS NULL OR p.product_name = :productName)\n" +
            "    AND (:brandName IS NULL OR b.brand_name = :brandName)\n" +
            "    AND (:typeName IS NULL OR t.type_name = :typeName)\n" +
            "    AND (:colorName IS NULL OR c.color_name = :colorName)\n" +
            "    AND (:materialName IS NULL OR m.material_name = :materialName)\n" +
            "    AND (:sizeName IS NULL OR s.size_name = :sizeName)\n" +
            "    AND (:soleName IS NULL OR so.sole_name = :soleName)\n" +
            "    AND (:genderName IS NULL OR g.gender_name = :genderName)\n" +
            "    AND (:status IS NULL OR pd.status = :status)\n" +
            "ORDER BY \n" +
            "    CASE \n" +
            "        WHEN COALESCE(:sortByQuantity, '') = 'ASC' THEN pd.quantity \n" +
            "        WHEN COALESCE(:sortByQuantity, '') = 'DESC' THEN pd.quantity \n" +
            "        ELSE null \n" +
            "    END,\n" +
            "    CASE \n" +
            "         WHEN COALESCE(:sortByPrice, '') = 'ASC' THEN pd.price  \n" +
            "         WHEN COALESCE(:sortByPrice, '') = 'DESC' THEN pd.price \n" +
            "        ELSE null \n" +
            "    END,\n" +
            "    pd.updated_at desc  \n" +
            "LIMIT :size OFFSET :offset ",
            nativeQuery = true)
    List<FilterProductDetailResponse> getFilterProductDetail(
            @Param("productName") String productName,
            @Param("brandName") String brandName,
            @Param("typeName") String typeName,
            @Param("colorName") String colorName,
            @Param("materialName") String materialName,
            @Param("sizeName") String sizeName,
            @Param("soleName") String soleName,
            @Param("genderName") String genderName,
            @Param("status") String status,
            @Param("sortByQuantity") String sortByQuantity,
            @Param("sortByPrice") String sortByPrice,
            @Param("offset") int offset,
            @Param("size") int size
    );


    //
    @Query(value = """
            SELECT COUNT(pd.id) AS total
            FROM product_detail pd
            JOIN product p ON pd.product_id = p.id
            JOIN brand b ON pd.brand_id = b.id
            JOIN type t ON pd.type_id = t.id
            JOIN color c ON pd.color_id = c.id
            JOIN material m ON pd.material_id = m.id
            JOIN size s ON pd.size_id = s.id
            JOIN sole so ON pd.sole_id = so.id
            JOIN gender g ON pd.gender_id = g.id
            WHERE\s
                (:productName IS NULL OR p.product_name = :productName)
                AND (:brandName IS NULL OR b.brand_name = :brandName)
                AND (:typeName IS NULL OR t.type_name = :typeName)
                AND (:colorName IS NULL OR c.color_name = :colorName)
                AND (:materialName IS NULL OR m.material_name = :materialName)
                AND (:sizeName IS NULL OR s.size_name = :sizeName)
                AND (:soleName IS NULL OR so.sole_name = :soleName)
                AND (:genderName IS NULL OR g.gender_name = :genderName)
                AND (:status IS NULL OR pd.status = :status);
                        
            """, nativeQuery = true)
    Integer getInforpage(@Param("productName") String productName,
                         @Param("brandName") String brandName,
                         @Param("typeName") String typeName,
                         @Param("colorName") String colorName,
                         @Param("materialName") String materialName,
                         @Param("sizeName") String sizeName,
                         @Param("soleName") String soleName,
                         @Param("genderName") String genderName,
                         @Param("status") String status

    );

    @Query(value = "SELECT *\n" +
                   "FROM product_detail\n" +
                   "WHERE product_id = :productId AND size_id = :sizeId AND color_id = :colorId\n And brand_id = :brandId" +
                   " and gender_id = :genderId and material_id =:materialId and type_id = :typeId and sole_id =:soleId"
            , nativeQuery = true)
    ProductDetail findByProductIdAndSizeIdAndColorId(Integer productId, Integer sizeId, Integer colorId, Integer
            brandId, Integer genderId, Integer materialId, Integer typeId, Integer soleId);


    List<ProductDetail> findByProductIdAndColorId(int productId, int colorId);

    @Query(value = "select  new com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse" +
                   "(pd.id,pd.code,pd.product.productName,pd.brand.brandName,pd.type.typeName,pd.color.colorName" +
                   ",pd.material.materialName,pd.size.sizeName,pd.sole.soleName,pd.gender.genderName,pd.quantity" +
                   ",pd.price,pd.weight,pd.descrition,pd.status,pd.updatedAt,pd.updatedBy) from ProductDetail pd" +
                   " where pd.product.productName LIKE :productDetailName order by pd.updatedAt desc ")
    Page<ProductDetailResponse> findByName(String productDetailName, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE product_detail SET status = :status WHERE product_id = :id", nativeQuery = true)
    void switchStatusPD(@Param("status") String status, @Param("id") Integer id);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<ProductDetail> findByIdWithLock(@Param("id") String id);


    //Em tú làm
    @Query("SELECT new com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse" +
           "(pd.id, pd.code, pd.product.productName, pd.brand.brandName, pd.type.typeName, pd.color.colorName, " +
           " pd.material.materialName, pd.size.sizeName, pd.sole.soleName, pd.gender.genderName, pd.quantity, " +
           " pd.price, pd.weight, pd.descrition, pd.status, pd.updatedAt, pd.updatedBy) " +
           "FROM ProductDetail pd WHERE pd.product.id = :productId ORDER BY pd.updatedAt DESC")
    List<ProductDetailResponse> findByProductId(@Param("productId") Integer productId);

    //Em tú hết làm
    @Query("SELECT new com.poly.app.domain.admin.product.response.productdetail.ProductAtributeExistResponse" +
           "(pd.id, pd.brand.id, pd.type.id) " +
           "FROM ProductDetail pd " +
           "WHERE pd.product.id = :productId " +
           "GROUP BY pd.id, pd.brand.id, pd.type.id, pd.updatedAt " +
           "ORDER BY pd.updatedAt DESC")
    List<ProductAtributeExistResponse> getAttributeOfProduct(@Param("productId") Integer productId);


}
