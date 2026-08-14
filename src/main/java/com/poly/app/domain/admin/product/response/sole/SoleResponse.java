package com.poly.app.domain.admin.product.response.sole;

import com.poly.app.infrastructure.constant.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SoleResponse {
    Integer id;
    String code;
    String soleName;
    Long updateAt;
    Status status;
}
