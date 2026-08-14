package com.poly.app.domain.admin.product.response.gender;

import com.poly.app.infrastructure.constant.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GenderResponse {
    Integer id;
    String code;
    String genderName;
    Long updateAt;
    Status status;
}
