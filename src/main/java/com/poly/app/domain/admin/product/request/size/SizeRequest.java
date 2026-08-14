package com.poly.app.domain.admin.product.request.size;

import com.poly.app.infrastructure.constant.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeRequest {
    @NotBlank(message = "NOT_BLANK")
    String sizeName;
    Status status;
}
