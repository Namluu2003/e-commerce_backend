package com.poly.app.domain.client.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionResponse {
    Integer promotionId;
    Integer productDetailId;
    Double discountValue;
    String promotionName;
}
