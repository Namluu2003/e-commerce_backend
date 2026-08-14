package com.poly.app.domain.repository;

import com.poly.app.domain.model.Bill;
import com.poly.app.domain.model.BillDetail;
import com.poly.app.domain.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail,Integer> {

    List<BillDetail> findByBill(Bill bill);
    List<BillDetail> findByBillId(Integer id);

    Optional<BillDetail> findByBillAndProductDetail(Bill bill, ProductDetail productDetail);
}
