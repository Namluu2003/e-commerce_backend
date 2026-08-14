package com.poly.app.domain.admin.bill.request;

import com.poly.app.domain.admin.address.AddressRequest;
import com.poly.app.domain.admin.product.request.productdetail.ProductDetailRequest;
import com.poly.app.domain.model.Customer;
import com.poly.app.domain.model.ProductDetail;
import com.poly.app.infrastructure.constant.BillStatus;
import com.poly.app.infrastructure.constant.TypeBill;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CreateBillRequest {
    private Integer customerId;
    private Double customerMoney;
    private Double cashCustomerMoney;
    private Double bankCustomerMoney;
    private Boolean isCashAndBank = false;
    private Double discountMoney;
    private Double shipMoney;
    private Double totalMoney;
    private Double moneyAfter;
    private LocalDateTime completeDate;
    private LocalDateTime confirmDate;
    private LocalDateTime desiredDateOfReceipt;
    private LocalDateTime shipDate;
    private Integer shippingAddressId;
    private String numberPhone;
    private String email;
    private TypeBill typeBill;
    private String notes;
    private BillStatus status;
    private Integer voucherId;
    private Boolean isShipping;
    private String recipientName;
    private String recipientPhoneNumber;
    private Double shippingFee;
    private AddressRequest address;
    private List<BillDetailRequest> billDetailRequests;
    private String transactionCode;
    private Boolean isCOD;
    private Double moneyBeforeDiscount;
    private Boolean isFreeShip;
}


