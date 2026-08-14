package com.poly.app.domain.admin.bill.service.impl;

import com.poly.app.domain.admin.bill.response.BillHistoryResponse;
import com.poly.app.domain.admin.bill.response.BillHistoryResponseBuilder;
import com.poly.app.domain.admin.bill.service.BillHistoryService;
import com.poly.app.domain.model.Bill;
import com.poly.app.domain.model.BillHistory;
import com.poly.app.domain.repository.BillHistoryRepository;
import com.poly.app.domain.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BillHistoryServiceImpl implements BillHistoryService {

    @Autowired
    BillHistoryRepository billHistoryRepository;

    @Autowired
    BillRepository billRepository;

    @Override
    public List<BillHistoryResponse> findBillHistoryResponseListByBillCode(String billCode) {
        return billHistoryRepository.findBillHistoryByBillCode(billCode);
    }

    @Override
    public List<BillHistoryResponseBuilder> findBillHistoryResponseBuilderListByBillCode(String billCode) {

        Bill bill = billRepository.findByBillCode(billCode);
        if(bill == null) {
            throw new RuntimeException();
        }
        List<BillHistory> billHistories = billHistoryRepository.findByBill(bill);
        System.out.println("billHistories" +billHistories.size());

        List<BillHistoryResponseBuilder> billHistoryResponseBuilders = billHistories.stream()
                .map( billHistory -> BillHistoryResponseBuilder
                        .builder()
                        .status(billHistory.getStatus())
                        .createdAt(billHistory.getCreatedAt())
                        .customerName(billHistory.getCustomer() != null ? billHistory.getCustomer().getFullName() : "")
                        .createdBy(billHistory.getCreatedBy())
                        .description(billHistory.getDescription())
                        .build() )
                .collect(Collectors.toList());

        Map<String, ?> result = new HashMap<>();






        return billHistoryResponseBuilders;
    }






}
