package com.poly.app.domain.model;

import com.poly.app.domain.model.base.PrimaryEntity;
import com.poly.app.infrastructure.constant.DiscountType;
import com.poly.app.infrastructure.constant.VoucherType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "voucher")
//phiếu giảm giá
public class Voucher extends PrimaryEntity implements Serializable {
    Integer id;
    //mã phiếu
    String voucherCode;
    String voucherName;
    //số lượng
    Integer quantity;
    //loại giảm giá
    @Enumerated(EnumType.STRING)
    VoucherType voucherType;
    @Enumerated(EnumType.STRING)
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
//    Status status;
   @Enumerated(EnumType.STRING)
    StatusEnum statusVoucher;

}
