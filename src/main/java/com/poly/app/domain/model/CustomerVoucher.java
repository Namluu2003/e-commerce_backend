package com.poly.app.domain.model;

import com.poly.app.domain.model.base.PrimaryEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "customer_voucher")
//khách hàng phiếu giảm giá
public class CustomerVoucher extends PrimaryEntity implements Serializable {
    @ManyToOne
    @JoinColumn
    Customer customer;
    @ManyToOne
    @JoinColumn
    Voucher voucher;
    Integer quantity;
}
