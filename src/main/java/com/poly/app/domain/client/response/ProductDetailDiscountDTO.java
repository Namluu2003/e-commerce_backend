package com.poly.app.domain.client.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDetailDiscountDTO {
    private Integer productId;
    private Integer productDetailId;
    private Integer colorId;
    private Integer genderId;
    private Double price;
    private Double maxDiscount;
    private String discountedPrice;

}