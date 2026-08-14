package com.poly.app.domain.client.request;

import com.poly.app.domain.admin.address.AddressRequest;
import com.poly.app.domain.admin.bill.request.BillDetailRequest;
import com.poly.app.infrastructure.constant.BillStatus;
import com.poly.app.infrastructure.constant.PaymentMethodsType;
import com.poly.app.infrastructure.constant.TypeBill;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateBillClientRequest {
    Integer customerId;
    Double customerMoney;
    Double discountMoney;
    Double shipMoney;
    Double totalMoney;
    Double moneyAfter;
    Double moneyBeforeDiscount;
    LocalDateTime desiredDateOfReceipt;
    LocalDateTime shipDate;
    Integer shippingAddressId;
    String email;
    String notes;
    Integer voucherId;
    String recipientName;
    String recipientPhoneNumber;
    AddressRequest detailAddressShipping;
    List<BillDetailRequest> billDetailRequests;
    //     pay
    PaymentMethodsType paymentMethodsType;
    String transactionCode;
    Boolean isFreeShip;

}
