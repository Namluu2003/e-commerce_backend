package com.poly.app.domain.admin.bill.response;

import com.poly.app.domain.admin.staff.response.AddressReponse;
import com.poly.app.domain.admin.voucher.response.VoucherReponse;
import com.poly.app.domain.admin.voucher.response.VoucherResponse;
import com.poly.app.domain.model.Address;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillResponse {

    private String billCode;
    private String customerName;
    private String customerPhone;
    private String staffName;
    private Double customerMoney;
    private Double discountMoney;
    private Double shipMoney;
    private Double totalMoney;
    private Double moneyBeforeDiscount;
    private String billType;
    private LocalDateTime completeDate;
    private LocalDateTime confirmDate;
    private LocalDateTime desiredDateOfReceipt;
    private LocalDateTime shipDate;
    private Address address;
    private String email;
    private String status;
    private String notes;
    private Long createAt;
    private VoucherReponse voucherReponse;
    private AddressReponse addressReponse;
    private Double moneyAfter;
    private Boolean isFreeShip;
}
