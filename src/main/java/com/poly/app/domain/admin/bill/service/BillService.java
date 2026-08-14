package com.poly.app.domain.admin.bill.service;

import com.poly.app.domain.admin.bill.request.BillProductDetailRequest;
import com.poly.app.domain.admin.bill.request.CreateBillRequest;
import com.poly.app.domain.admin.bill.request.UpdateProductBillRequest;
import com.poly.app.domain.admin.bill.request.UpdateQuantityVoucherRequest;
import com.poly.app.domain.admin.bill.request.UpdateStatusBillRequest;
import com.poly.app.domain.admin.bill.response.BillResponse;
import com.poly.app.domain.admin.bill.response.UpdateBillRequest;
import com.poly.app.domain.admin.voucher.response.VoucherReponse;
import com.poly.app.infrastructure.constant.BillStatus;
import com.poly.app.infrastructure.constant.TypeBill;
import org.springframework.data.domain.Page;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface BillService {

    Page<BillResponse> getPageBill(Integer size, Integer page, BillStatus statusBill, TypeBill typeBill, String search, String startDate, String endDate) ;

    BillResponse getBillResponseByBillCode(String billCode);


    Map<String,?> updateStatusBill(String billCode, UpdateStatusBillRequest request);

    BillResponse updateBillInfo(String billCode, UpdateBillRequest request);

    File printBillById(String billCode);

    BillResponse createBill(CreateBillRequest request);

    void updateProductQuantity(List<BillProductDetailRequest> requests);


    List<VoucherReponse> getAllVoucherResponse();
    List<VoucherReponse> getAllVoucherResponseByCustomerId(Integer customerId);

    VoucherReponse updateQuantityVoucher(UpdateQuantityVoucherRequest request);

    List<Map<String, Object>> getBillCountByStatus();

    void updateProductBill(String billCode, UpdateProductBillRequest request);


}
