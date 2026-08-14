package com.poly.app.domain.admin.bill.controller;


import com.poly.app.domain.admin.bill.service.BillHistoryService;
import com.poly.app.domain.common.ApiResponse;
import com.poly.app.domain.model.BillHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/bill-history")
public class BillHistoryController {

    @Autowired
    BillHistoryService billHistoryService;

    @GetMapping
    public ApiResponse<?> getBillHistoryByBillCode(@RequestParam String billCode) {
        return ApiResponse.builder().data(billHistoryService.findBillHistoryResponseBuilderListByBillCode(billCode)).build();
    }

}
