package com.poly.app.domain.model;

import com.poly.app.domain.model.base.PrimaryEntity;
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
@ToString
@Table(name = "image")
//ảnh
public class Image extends PrimaryEntity implements Serializable {

    @ManyToOne
    @JoinColumn
    ProductDetail productDetail;

    Boolean isDefault;

    String url;

    String publicId;

    Status status;

}
