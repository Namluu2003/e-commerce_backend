package com.poly.app.domain.model;

import com.poly.app.domain.model.base.PrimaryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "freeship_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FreeshipOrder extends PrimaryEntity implements Serializable {


    @Column(name = "min_order_value", nullable = false)
    private Double minOrderValue;

}
