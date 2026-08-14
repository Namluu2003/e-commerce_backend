package com.poly.app.domain.admin.product.request.color;

import com.poly.app.infrastructure.constant.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorRequest {
    @NotBlank(message = "NOT_BLANK")
    String colorName;
    Status status;
    String code;
}
