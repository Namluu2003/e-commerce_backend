package com.poly.app.domain.admin.product.response.material;

import com.poly.app.infrastructure.constant.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaterialResponse {
    Integer id;
    String code;
    String materialName;
    Long updateAt;
    Status status;
}
