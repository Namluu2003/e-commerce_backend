package com.poly.app.domain.admin.product.response.productdetail;

import com.poly.app.domain.admin.product.response.img.ImgResponse;
import com.poly.app.domain.client.response.PromotionResponse;
import com.poly.app.domain.model.ProductDetail;
import com.poly.app.infrastructure.constant.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductAtributeExistResponse {
    Integer productId;
    Integer brandId;
    Integer typeId;
}
