package com.poly.app.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.poly.app.domain.model.base.PrimaryEntity;
import com.poly.app.infrastructure.constant.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "product")
//hang
public class Product extends PrimaryEntity implements Serializable {

    String productName;

    Integer views;

    Status status;


    @JsonManagedReference
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetail> productDetails = new ArrayList<>();

}
