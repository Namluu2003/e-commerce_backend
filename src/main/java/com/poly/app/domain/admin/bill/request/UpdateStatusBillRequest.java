package com.poly.app.domain.admin.bill.request;

import com.poly.app.infrastructure.constant.BillStatus;
import com.poly.app.infrastructure.constant.PaymentMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdateStatusBillRequest  {

    BillStatus status;
    String note;
    String transactionCode;
    PaymentMethodEnum paymentMethodEnum;


    String provinceId; // tỉnh
    String districtId; // quận
    String wardId; // xã
    String specificAddress;




}
