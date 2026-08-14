package com.poly.app.domain.admin.bill.service;

import com.poly.app.domain.admin.bill.response.PaymentBillResponse;
import com.poly.app.domain.model.PaymentBill;

import java.util.List;

public interface PaymentBillService {
    List<PaymentBillResponse> findByPaymentBill(String billCode);


}
