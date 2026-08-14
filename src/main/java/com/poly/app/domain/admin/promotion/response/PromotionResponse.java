package com.poly.app.domain.admin.promotion.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PromotionResponse {
    Integer id;
    String promotionCode;
    String promotionName;
    String promotionType;
    Double discountValue;
    DiscountType discountType;
    Integer quantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime endDate;
    StatusEnum statusPromotion;
    List<Integer> productIds;
    List<Integer> productDetailIds;

    public static PromotionResponse fromEntity(Promotion promotion) {
        if (promotion == null) {
            return null;
        }
        return PromotionResponse.builder()
                .id(promotion.getId())
                .promotionCode(promotion.getPromotionCode())
                .promotionName(promotion.getPromotionName())
                .promotionType(promotion.getPromotionType())
                .discountValue(promotion.getDiscountValue())
                .discountType(promotion.getDiscountType())
                .quantity(promotion.getQuantity())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .statusPromotion(promotion.getStatusPromotion())
                .build();
    }


}
