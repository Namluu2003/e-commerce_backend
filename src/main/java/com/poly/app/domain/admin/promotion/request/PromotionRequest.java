package com.poly.app.domain.admin.promotion.request;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionRequest {
    Integer id;
    // mã giảm
    String promotionCode;
    // tên loại giảm
    String promotionName;
    // loại giảm
    String promotionType;
    // giá trị giảm
    Double discountValue;
    DiscountType discountType;
    Integer quantity;
    // ngày bắt đầu
    LocalDateTime startDate;
    // ngày kết thúc
    LocalDateTime endDate;
    // trạng thái
    StatusEnum statusPromotion;
    List<Integer> productIds;
    List<Integer> productDetailIds;

}
