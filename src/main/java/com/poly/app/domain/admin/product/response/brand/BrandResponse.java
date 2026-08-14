package com.poly.app.domain.admin.product.response.brand;

import com.poly.app.infrastructure.constant.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandResponse {
    Integer id;
    String code;
    String brandName;
    Long updateAt;
    Status status;
}
