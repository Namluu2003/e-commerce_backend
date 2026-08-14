package com.poly.app.domain.model;

import com.poly.app.domain.model.base.PrimaryEntity;
import com.poly.app.infrastructure.constant.Status;
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
@Table(name = "bill_detail")
//hóa đơn chi tiết
public class BillDetail extends PrimaryEntity implements Serializable {

    @ManyToOne
    @JoinColumn
    Bill bill;

    @ManyToOne
    @JoinColumn
    ProductDetail productDetail;

    Double price;

    Integer quantity;

    Double totalMoney;

    Status status;

    String image;

}
