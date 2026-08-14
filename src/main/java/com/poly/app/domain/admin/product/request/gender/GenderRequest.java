package com.poly.app.domain.admin.product.request.gender;

import com.poly.app.infrastructure.constant.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GenderRequest {
    @NotBlank(message = "NOT_BLANK")
    String genderName;
    Status status;
}
