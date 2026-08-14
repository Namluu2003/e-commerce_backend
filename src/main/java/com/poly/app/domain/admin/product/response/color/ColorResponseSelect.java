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
public class ColorResponseSelect {
    Integer id;
    String ColorName;
    Status status;
    String code;
}
