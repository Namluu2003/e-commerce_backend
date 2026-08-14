package com.poly.app.domain.admin.voucher.service;

import com.poly.app.domain.admin.bill.response.BillResponse;
import com.poly.app.domain.admin.voucher.request.voucher.VoucherRequest;
import com.poly.app.domain.admin.voucher.response.VoucherReponse;
import com.poly.app.domain.auth.request.RegisterRequest;
import com.poly.app.domain.model.StatusEnum;
import com.poly.app.domain.model.Voucher;
import com.poly.app.infrastructure.constant.DiscountType;
import com.poly.app.infrastructure.constant.VoucherType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface VoucherService {
    List<VoucherReponse> getAllVoucher();

    Voucher createVoucher(VoucherRequest request);

    VoucherReponse updateVoucher(VoucherRequest request, int id);

    String deleteVoucher(int id);

    VoucherReponse getVoucherDetail(int id);

    Page<VoucherReponse> getAllVoucher(Pageable pageable);

    Boolean register(RegisterRequest request);

    String switchStatus(Integer id, StatusEnum status);


    List<VoucherReponse> getAllVouchersWithCustomer(Integer customerId);



    Page<VoucherReponse> getPageVoucher(int size,
                                        int page,
                                        StatusEnum statusVoucher,
                                        String search,
                                        String startDate,
                                        String endDate,
                                        VoucherType voucherType,
                                        DiscountType discountType
    );

}
