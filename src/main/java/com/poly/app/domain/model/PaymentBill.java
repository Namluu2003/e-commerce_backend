package com.poly.app.domain.model;

import com.poly.app.domain.model.base.PrimaryEntity;
import com.poly.app.infrastructure.constant.PayMentBillStatus;
import com.poly.app.infrastructure.constant.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "payment_bill")
//thanh toán háo dơn
public class PaymentBill extends PrimaryEntity implements Serializable {

    @ManyToOne
    @JoinColumn
    Bill bill;

    @ManyToOne
    @JoinColumn
    PaymentMethods paymentMethods;

    Double totalMoney; // Tổng tiền

    String notes; // Ghi chú

    String transactionCode; // Mã giao dịch

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    PayMentBillStatus payMentBillStatus;

}
