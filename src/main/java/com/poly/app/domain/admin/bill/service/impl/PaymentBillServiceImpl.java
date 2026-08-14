package com.poly.app.domain.admin.bill.service.impl;

import com.poly.app.domain.admin.bill.response.PaymentBillResponse;
import com.poly.app.domain.admin.bill.service.PaymentBillService;
import com.poly.app.domain.model.Bill;
import com.poly.app.domain.model.PaymentBill;
import com.poly.app.domain.repository.BillRepository;
import com.poly.app.domain.repository.PaymentBillRepository;
import com.poly.app.domain.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentBillServiceImpl implements PaymentBillService {

    @Autowired
    BillRepository billRepository;

    @Autowired
    PaymentBillRepository paymentBillRepository;



    @Override
    public List<PaymentBillResponse> findByPaymentBill(String billCode) {

        Bill bill  = billRepository.findByBillCode(billCode);

        List<PaymentBillResponse> paymentBillList = paymentBillRepository.findPaymentBillByBillCode(bill.getCode());

        return paymentBillList;
    }
}
