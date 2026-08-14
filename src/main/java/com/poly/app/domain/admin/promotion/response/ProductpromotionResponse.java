package com.poly.app.domain.admin.promotion.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poly.app.domain.model.Product;
import com.poly.app.domain.model.ProductDetail;
import com.poly.app.domain.model.Promotion;
import com.poly.app.domain.model.StatusEnum;
import com.poly.app.infrastructure.constant.DiscountType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductpromotionResponse {
    List<Product> productIds;
    List<ProductDetail> productDetailIds;


}
