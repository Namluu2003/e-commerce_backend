package com.poly.app.domain.model;

import com.poly.app.domain.model.base.PrimaryEntity;
import com.poly.app.infrastructure.constant.BillStatus;
import com.poly.app.infrastructure.constant.TypeBill;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "bill")
//hóa đơn
public class    Bill extends PrimaryEntity implements Serializable {

    @ManyToOne
    @JoinColumn
    Customer customer;

    @ManyToOne
    @JoinColumn
    Staff staff;


    String billCode;

    @PrePersist
    public void generateId() {
        if (this.billCode == null || this.billCode.isEmpty()) {
            this.billCode = "HD-" + UUID.randomUUID().toString().replace("-", "").toUpperCase().substring(0, 8);
        }
    }

    Double customerMoney;

    // Tiền giảm giá
    Double discountMoney;

    // Tiền ship
    Double shipMoney;

    // Tiền sau giảm giá đã trừ voucher
    Double totalMoney;

    // Tiền sau giảm giá + tiêền ship
    Double moneyAfter;// Tiền cuối cùng mà khách hàng cần thanh toán //

    // Tiền trước áp phiếu giảm là : Tổng tiền hàng --- đã được
    Double moneyBeforeDiscount;

    //nagyf hoàn thành
    LocalDateTime completeDate;
    //    nagyf xác nhận
    LocalDateTime confirmDate;
    //    ngày mong muốn nhận hàng
    LocalDateTime desiredDateOfReceipt;
    //    ngày ship
    LocalDateTime shipDate;

    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    Address shippingAddress;

    String customerName;

    String numberPhone;

    String email;

    @Enumerated(EnumType.STRING)
    TypeBill typeBill;

    String notes;

    // Phụ phí
    String surcharge;
    // Phụ phí notes
    String surchargeNotes;

    Boolean isFreeShip;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    BillStatus status;

    @ManyToOne
    @JoinColumn(name = "voucher_id", referencedColumnName = "id")
    private Voucher voucher;  //

}
