package com.poly.app.domain.admin.product.request.product;

import com.poly.app.infrastructure.constant.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    String ProductName;
    Status status;
}
