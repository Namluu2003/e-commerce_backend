package com.poly.app.domain.repository;

import com.poly.app.domain.client.response.CartResponse;
import com.poly.app.domain.model.CartDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Integer> {
    @Query(value = "select new com.poly.app.domain.client.response.CartResponse" +
                   "(cd.id,cd.productDetailId.id,cd.productName,cd.price,cd.quantity,cd.imageUrl)" +
                   " from CartDetail cd " +
                   "where cd.cart.customerid.id=:customerId " +
                   "order by cd.createdAt desc")
    Page<CartResponse> getAllByCustomerId(@Param("customerId") Integer customerId, Pageable pageable);

    @Query(value = "select cd from CartDetail cd where cd.productDetailId.id = :productDetailId and cd.cart.customerid.id = :customerId")
    CartDetail findByProductDetailId(@Param("productDetailId") Integer productDetailId, @Param("customerId") Integer customerId);

    @Query(value = "select new com.poly.app.domain.client.response.CartResponse" +
                   "(cd.id,cd.productDetailId.id,cd.productName,cd.price,cd.quantity,cd.imageUrl)" +
                   " from CartDetail cd " +
                   "where cd.cart.customerid.id=:customerId " +
                   "order by cd.createdAt desc")
    List<CartResponse> getAllByCustomerIdNoPage(@Param("customerId") Integer customerId);
}
