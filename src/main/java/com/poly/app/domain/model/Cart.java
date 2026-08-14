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
@Table(name = "cart")
//giỏ hàng
public class Cart extends PrimaryEntity implements Serializable {

    @OneToOne
    @JoinColumn(unique = true)
    Customer customerid;



}
