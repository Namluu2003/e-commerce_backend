package com.poly.app.domain.admin.product.response.color;

import com.poly.app.infrastructure.constant.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorResponse {
    Integer id;
    String code;
    String colorName;
    Long updateAt;
    Status status;
}
