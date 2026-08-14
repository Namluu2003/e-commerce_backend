package com.poly.app.domain.client.response;

import com.poly.app.domain.admin.address.AddressRequest;
import com.poly.app.domain.model.Address;
import com.poly.app.infrastructure.constant.BillStatus;
import com.poly.app.infrastructure.constant.TypeBill;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchStatusBillResponse {
    Integer id;

    String billCode;

    Double discountMoney;

    Double shipMoney;

    Double totalMoney;

    Double moneyAfter;

    Integer shippingAddress;

    String customerName;

    String numberPhone;

    String email;

    TypeBill typeBill;

    String notes;

    BillStatus status;

    String voucher;

    String payment;

    List<BillDetailResponse> billDetailResponse;

    AddressRequest addressRequest;

}
