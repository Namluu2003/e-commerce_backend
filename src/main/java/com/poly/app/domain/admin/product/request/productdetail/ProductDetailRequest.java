package com.poly.app.domain.admin.product.request.productdetail;

import com.poly.app.domain.admin.product.request.img.ImgRequest;
import com.poly.app.infrastructure.constant.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailRequest {
    Integer productId;

    Integer brandId;

    Integer typeId;

    Integer colorId;

    Integer materialId;

    Integer sizeId;

    Integer soleId;

    Integer genderId;

    Integer quantity;

    Double price;

    Double weight;

    String description;

    Status status;

    List<ImgRequest> image;
}
