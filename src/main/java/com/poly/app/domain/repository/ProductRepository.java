package com.poly.app.domain.repository;

import com.poly.app.domain.admin.product.response.product.IProductResponse;
import com.poly.app.domain.admin.product.response.product.ProductResponseSelect;
import com.poly.app.domain.admin.product.response.product.ProductResponse;
import com.poly.app.domain.admin.product.response.product.ProductResponse;
import com.poly.app.domain.model.Product;
import com.poly.app.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "SELECT \n" +
            "    p.id, \n" +
            "    p.code, \n" +
            "    p.product_name AS productName, \n" +
            "    COALESCE(SUM(pd.quantity), 0) AS totalQuantity, \n" +
            "    p.updated_at AS updatedAt, \n" +
            "    p.status, \n" +
            "    MAX(pd.updated_at) AS lastUpdated\n" +
            "FROM product p \n" +
            "left JOIN product_detail pd ON p.id = pd.product_id \n" +
            "WHERE p.product_name LIKE :name " +
            "GROUP BY p.id, p.code, p.product_name, p.updated_at, p.status\n" +
            "ORDER BY lastUpdated DESC",
            nativeQuery = true)
    Page<IProductResponse> fillbyname(String name, Pageable pageable);

    @Query(value = "SELECT \n" +
            "    p.id, \n" +
            "    p.code, \n" +
            "    p.product_name AS productName, \n" +
            "    COALESCE(SUM(pd.quantity), 0) AS totalQuantity, \n" +
            "    p.updated_at AS updatedAt, \n" +
            "    p.status, \n" +
            "    MAX(pd.updated_at) AS lastUpdated\n" +
            "FROM product p \n" +
            "left JOIN product_detail pd ON p.id = pd.product_id \n" +
            "GROUP BY p.id, p.code, p.product_name, p.updated_at, p.status\n" +
            "ORDER BY lastUpdated DESC",
            nativeQuery = true)
    Page<IProductResponse> getAll(Pageable pageable);

    boolean existsByProductName(String name);

    boolean existsByProductNameAndIdNot(String name, Integer id);

    @Query(value = "select new com.poly.app.domain.admin.product.response.product.ProductResponseSelect(b.id,b.productName,b.status) from Product b order by b.createdAt desc ")
    List<ProductResponseSelect> dataSelect();
    @Query(value = "select new com.poly.app.domain.admin.product.response.product.ProductResponseSelect(b.id,b.productName,b.status) from Product b where b.status=0 order by b.createdAt desc ")
    List<ProductResponseSelect> dataSelecthd();
    //respone lấy danh sách theo tên bla bla

    //    Optional<ProductDetail> findByProductName(String productName);
//em tú làm
    @Query(value = "SELECT p.product_name, COALESCE(SUM(pd.quantity), 0) AS total_quantity " +
            "FROM product p " +
            "LEFT JOIN product_detail pd ON p.id = pd.product_id " +
            "WHERE p.product_name LIKE %:productName% " +
            "GROUP BY p.product_name",
            nativeQuery = true)
    List<Object[]> findProductQuantityByName(@Param("productName") String productName);

    @Query(value = "SELECT p.product_name, COALESCE(SUM(pd.quantity), 0) AS total_quantity " +
            "FROM product p " +
            "LEFT JOIN product_detail pd ON p.id = pd.product_id " +
            "GROUP BY p.product_name " +
            "HAVING total_quantity <= :minQuantity",
            nativeQuery = true)
    List<Object[]> findProductByMinQuantity(@Param("minQuantity") int minQuantity);

    //em tú hết làm



}
