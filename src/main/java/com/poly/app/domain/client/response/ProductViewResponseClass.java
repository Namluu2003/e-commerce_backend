package com.poly.app.domain.client.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductViewResponseClass {
    private Integer productId;
    private String productName;
    private Long productDetailId;
    private String price;
    private Integer sold;
    private Integer tag;
    private Integer colorId;
    private Integer sizeId;
    private Integer genderId;
    private Integer materialId;
    private Integer soleId;
    private String imageUrl;
    private Long createdAt;
    private Long views;
    private PromotionView promotionView;

}