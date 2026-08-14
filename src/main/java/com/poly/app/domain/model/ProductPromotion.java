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
@Table(name = "product_promotion")
public class ProductPromotion extends PrimaryEntity implements Serializable {

    Integer product;
    @ManyToOne
    @JoinColumn
    ProductDetail productDetail;
    @ManyToOne
    @JoinColumn
    Promotion promotion;
    Integer quantity;
}
