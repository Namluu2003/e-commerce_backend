package com.poly.app.domain.admin.bill.controller;


import com.poly.app.domain.admin.bill.service.PaymentBillService;
import com.poly.app.domain.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/payment-bill")
public class PaymentBillController {

    @Autowired
    private PaymentBillService paymentBillService;



    @GetMapping()
    public ApiResponse<?> getPaymentBill(
            @RequestParam String billCode
    ) {
        return ApiResponse.builder().data(paymentBillService.findByPaymentBill(billCode)).build();
    }

}
