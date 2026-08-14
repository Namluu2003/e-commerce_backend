package com.poly.app.domain.model;

import com.poly.app.domain.model.base.PrimaryEntity;
import com.poly.app.infrastructure.constant.PaymentMethodEnum;
import com.poly.app.infrastructure.constant.PaymentMethodsType;
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
@Table(name = "payment_methods")
public class PaymentMethods extends PrimaryEntity implements Serializable {

    // COD hay Thanh toán trước

    // Đang thanh toán bằng chuyển khoản hay tiền mặt

    // Thanh toán hay hoàn tiền

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    PaymentMethodsType paymentMethodsType; // Loại thanh toán (enum)

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    PaymentMethodEnum paymentMethod; // Phương thước thanh toán

    @Enumerated(EnumType.STRING)
    Status status; // Trạng thái (enum)


}
