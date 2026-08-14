package com.poly.app.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.poly.app.domain.model.base.PrimaryEntity;

import com.poly.app.infrastructure.constant.EntityProperties;
import com.poly.app.infrastructure.constant.Tag;
import jakarta.persistence.*;

import com.poly.app.infrastructure.constant.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "product_detail")
//sản phẩm jchi tiết
public class ProductDetail extends PrimaryEntity implements Serializable {
    @ManyToOne
    @JoinColumn
    @JsonBackReference
    Product product;

    @ManyToOne
    @JoinColumn
    Brand brand;

    @ManyToOne
    @JoinColumn
    Type type;

    @ManyToOne
    @JoinColumn
    Color color;

    @ManyToOne
    @JoinColumn
    Material material;

    @ManyToOne
    @JoinColumn
    Size size;

    @ManyToOne
    @JoinColumn
    Sole sole;

    @ManyToOne
    @JoinColumn
    Gender gender;

    String productDetailCode;

    private Integer quantity;

    @PrePersist
    @PreUpdate
    private void validateQuantity() {
        if (this.quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }


    Double price;

    Double weight;

    @Column(columnDefinition = EntityProperties.DEFINITION_DESCRIPTION)
    String descrition;

    Integer sold;

    Tag tag;
    Status status;



}
