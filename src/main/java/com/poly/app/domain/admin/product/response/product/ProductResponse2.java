package com.poly.app.domain.admin.product.response.product;

import com.poly.app.domain.model.Product;
import com.poly.app.infrastructure.constant.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse2 {
    Integer id;
    String code;
    String productName;
    Integer totalQuantity;
    Long updateAt;
    Byte status;
    String image;


}
