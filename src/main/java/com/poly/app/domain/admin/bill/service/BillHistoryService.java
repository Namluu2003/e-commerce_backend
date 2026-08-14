package com.poly.app.domain.admin.bill.service;

import com.poly.app.domain.admin.bill.response.BillHistoryResponse;
import com.poly.app.domain.admin.bill.response.BillHistoryResponseBuilder;

import java.util.List;

public interface BillHistoryService {

    List<BillHistoryResponse> findBillHistoryResponseListByBillCode(String billCode);

    List<BillHistoryResponseBuilder> findBillHistoryResponseBuilderListByBillCode(String billCode);

}
