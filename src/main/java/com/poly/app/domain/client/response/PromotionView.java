package com.poly.app.domain.client.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionView {
//    Khoảng giá gốc
    String rangePriceRoot;
//    Khoảng giá sau giảm
    String rangePriceAfterPromotion;
//    giảm cao nhất
    String maxDiscount;
}
