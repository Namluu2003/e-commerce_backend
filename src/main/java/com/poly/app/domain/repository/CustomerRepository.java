
package com.poly.app.domain.repository;

import com.poly.app.domain.model.Customer;
import com.poly.app.domain.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {

    Customer findByEmail(String email);
    Customer findByPhoneNumber(String phoneNumber);
    Optional<Customer> findCustomerByEmailAndPassword(String email, String password);


    boolean existsCustomerByEmail(String email);


    @Query("SELECT c FROM Customer c WHERE " +
            "LOWER(c.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "c.phoneNumber LIKE CONCAT('%', :keyword, '%')")
    List<Customer> searchByKeyword(@Param("keyword") String keyword);

    Customer findByTokenActiveAccount(String tokenActiveAccount);

    boolean existsCustomersByPhoneNumber(String phoneNumber);
}





