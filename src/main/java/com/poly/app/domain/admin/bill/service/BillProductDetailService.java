package com.poly.app.domain.admin.bill.service;

import com.poly.app.domain.admin.bill.request.AddProductToBillRequest;
import com.poly.app.domain.admin.bill.response.BillProductDetailResponse;
import com.poly.app.domain.admin.bill.response.BillProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BillProductDetailService {

    List<BillProductDetailResponse> getBillProductDetailResponse(String billCode);

    Page<BillProductResponse> getBillProductResponsePage(int page, int size);

    void addProductToBill (AddProductToBillRequest request);
}
