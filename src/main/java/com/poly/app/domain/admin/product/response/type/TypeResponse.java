package com.poly.app.domain.admin.product.response.type;

import com.poly.app.infrastructure.constant.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeResponse {
    Integer id;
    String code;
    String typeName;
    Long updateAt;
    Status status;
}
