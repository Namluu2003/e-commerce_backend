package com.poly.app.domain.repository;

import com.poly.app.domain.admin.bill.response.BillHistoryResponse;
import com.poly.app.domain.model.Bill;
import com.poly.app.domain.model.BillHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillHistoryRepository extends JpaRepository<BillHistory,Integer> {


    @Query("select " +
            "bh.staff.fullName as staffName, " +
            "bh.customer.fullName as customerName, " +
            "b.code as billCode," +
            "bh.status as status," +
            "bh.description as description, " +
            "bh.createdAt as createdAt" +
            " from BillHistory bh  LEFT JOIN Bill b on bh.bill.id = b.id where b.code = :billCode ")
    List<BillHistoryResponse> findBillHistoryByBillCode(String billCode);


    List<BillHistory> findByBill(Bill bill);

    BillHistory findDistinctFirstByBillOrderByCreatedAtDesc(Bill bill);

    @Query("SELECT CASE WHEN COUNT(bh) > 0 THEN true ELSE false END " +
            "FROM BillHistory bh " +
            "WHERE bh.bill = :bill AND bh.status = 'DA_THANH_TOAN'")
    Boolean existsByBillAndStatusDaThanhToan(@Param("bill") Bill bill);

}
