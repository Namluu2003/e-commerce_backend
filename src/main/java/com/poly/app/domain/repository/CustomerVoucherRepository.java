package com.poly.app.domain.repository;

import com.poly.app.domain.model.Customer;
import com.poly.app.domain.model.CustomerVoucher;
import com.poly.app.domain.model.Voucher;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerVoucherRepository extends JpaRepository<CustomerVoucher,Integer> {

    List<CustomerVoucher> findCustomerVouchersByVoucher(Voucher voucher);
    void deleteByCustomerAndVoucher(Customer customer, Voucher voucher);
    @Modifying
    @Transactional
    @Query("DELETE FROM CustomerVoucher cv WHERE cv.voucher.id = :voucherId")
    void deleteByVoucherId(@Param("voucherId") int voucherId);


    List<CustomerVoucher> findCustomerVouchersByCustomerId(Integer customerId);


    CustomerVoucher findCustomerVouchersByVoucherAndCustomer_Id(Voucher voucher, Integer customerId);

}
