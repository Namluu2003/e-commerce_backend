package com.poly.app.domain.client.response;

import com.poly.app.infrastructure.constant.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RealPriceResponse {
    Integer cartDetailId;
    Integer productDetailId;
    String productName;
    Double price;
    Integer quantityAddCart;
    Integer quantity;
    String image;
    String note;
    Status status;
}
