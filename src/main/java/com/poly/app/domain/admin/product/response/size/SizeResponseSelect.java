package com.poly.app.domain.admin.product.response.size;

import com.poly.app.infrastructure.constant.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeResponseSelect {
    Integer id;
    String sizeName;
    Status status;
}
