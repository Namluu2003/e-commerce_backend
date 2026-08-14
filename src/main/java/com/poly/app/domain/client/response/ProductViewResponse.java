package com.poly.app.domain.client.response;

import java.math.BigDecimal;


public interface ProductViewResponse {
    Integer getProductId();
    String getProductName();
    Long getProductDetailId();
    String getPrice();
    Integer getSold();
    Integer getTag();
    Integer getColorId();
    Integer getSizeId();
    Integer getMaterialId();
    Integer getSoleId();
    String getImageUrl();
    Integer getGenderId();
    String getPromotionName();
    String getDiscountValue();
    String getPromotionType();
    Long getCreatedAt();
    Long getViews();
}
