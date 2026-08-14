package com.poly.app.domain.admin.voucher.request.voucher;

import com.poly.app.domain.model.StatusEnum;
import com.poly.app.infrastructure.constant.DiscountType;
import com.poly.app.infrastructure.constant.VoucherType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherRequest {
    //phiếu giảm giá
        Integer id;
        //mã phiếu
        String voucherCode;
    String voucherName;
        //số lượng
        Integer quantity;
        //loại giảm
        VoucherType voucherType;
        DiscountType discountType;
        //giá trị giảm
        Double discountValue;
        String discountValueType;
    //giá trị giảm tối đa
        Double discountMaxValue;
    //giá trị giảm tối thiểu
        Double billMinValue;
        LocalDateTime startDate;
        LocalDateTime endDate;
        StatusEnum statusVoucher;
        List<String> gmailkh;
        Integer loaivoucher;
}
