package com.poly.app.domain.repository;

import com.poly.app.domain.admin.bill.response.PaymentBillResponse;
import com.poly.app.domain.model.Bill;
import com.poly.app.domain.model.PaymentBill;
import com.poly.app.domain.model.PaymentMethods;
import com.poly.app.infrastructure.constant.PayMentBillStatus;
import com.poly.app.infrastructure.constant.PaymentMethodEnum;
import com.poly.app.infrastructure.constant.PaymentMethodsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentBillRepository extends JpaRepository<PaymentBill, Integer> {


    List<PaymentBill> findByBill(Bill bill);

    PaymentBill findDistinctFirstByBill(Bill bill);

    PaymentBill findByBillId(Integer id);
//e công sửa 2 trường paymentbillstatus với updateat ->
    @Query(value = "SELECT " +
            "pm.paymentMethodsType AS paymentMethodsType, " +
            "pm.paymentMethod AS paymentMethod, " +
            "pb.notes AS notes, " +
            "pb.totalMoney AS totalMoney, " +
            "pb.updatedAt AS createdAt, " +
            "pb.payMentBillStatus AS paymentBillStatus, " +
            "pb.transactionCode AS transactionCode " +
            "FROM PaymentBill pb " +
            "LEFT JOIN PaymentMethods pm ON pm.id = pb.paymentMethods.id " +
            "LEFT JOIN Bill b ON b.id = pb.bill.id " +
            "WHERE b.code = :billCode")
    List<PaymentBillResponse> findPaymentBillByBillCode(@Param("billCode") String billCode);

    @Query(value = "select pb from PaymentBill pb where pb.payMentBillStatus = :payMentBillStatus " +
                   "and pb.paymentMethods.paymentMethod = :paymentMethod " +
                   "and pb.createdAt >= :timeLimit")
    List<PaymentBill> getAllPaymentBillCTT(@Param("payMentBillStatus") PayMentBillStatus payMentBillStatus,
                                           @Param("paymentMethod") PaymentMethodEnum paymentMethod,
                                           @Param("timeLimit") Long timeLimit);
}
