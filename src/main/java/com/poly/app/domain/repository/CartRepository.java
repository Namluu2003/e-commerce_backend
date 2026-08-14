package com.poly.app.domain.repository;

import com.poly.app.domain.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query(value = "select c from Cart c where c.customerid.id =:customerId ")
    Cart getCart(@Param("customerId") Integer customerId);

}
