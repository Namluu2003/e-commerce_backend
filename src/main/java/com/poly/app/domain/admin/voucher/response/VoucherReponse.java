package com.poly.app.domain.admin.voucher.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poly.app.domain.model.StatusEnum;
import com.poly.app.domain.model.Voucher;
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
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherReponse {
    Integer id;
    String voucherCode;
    String voucherName;
    Integer quantity;
    VoucherType voucherType;
    Double discountValue;
    Double discountMaxValue;
    Double billMinValue;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime endDate;
    StatusEnum statusVoucher;
    String discountValueType;
    DiscountType discountType;
    List<Integer> customerIds;


    public static VoucherReponse formEntity(Voucher voucher) {
        if (voucher == null) {
            return null;
        }

        return VoucherReponse.builder()
                .id(voucher.getId())
                .voucherCode(voucher.getVoucherCode())
                .voucherName(voucher.getVoucherName())
                .quantity(voucher.getQuantity())
                .discountValue(voucher.getDiscountValue())
                .discountMaxValue(voucher.getDiscountMaxValue())
                .billMinValue(voucher.getBillMinValue())
                .startDate(voucher.getStartDate())
                .endDate(voucher.getEndDate())
                .discountType(voucher.getDiscountType())
                .voucherType(voucher.getVoucherType())
                .statusVoucher(voucher.getStatusVoucher())
                .discountValueType(voucher.getDiscountValueType())
                .build();
    }

}
