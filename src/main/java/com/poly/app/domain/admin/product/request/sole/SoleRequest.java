package com.poly.app.domain.admin.product.request.sole;

import com.poly.app.infrastructure.constant.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SoleRequest {
    @NotBlank(message = "NOT_BLANK")
    String soleName;
    Status status;
}
