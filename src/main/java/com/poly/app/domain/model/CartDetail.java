package com.poly.app.domain.model;

import com.poly.app.domain.model.base.PrimaryEntity;
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
@Table(name = "cart_detail")
//giỏ hàng
public class CartDetail extends PrimaryEntity implements Serializable {

    @ManyToOne
    @JoinColumn
    Cart cart;

    @ManyToOne
    @JoinColumn
    ProductDetail productDetailId;

    String imageUrl;

    Integer quantity;
    Double price;
    String productName;
}
