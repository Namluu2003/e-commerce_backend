package com.poly.app.domain.admin.product.request.material;

import com.poly.app.infrastructure.constant.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaterialRequest {
    @NotBlank(message = "NOT_BLANK")
    String MaterialName;
    Status status;
}
