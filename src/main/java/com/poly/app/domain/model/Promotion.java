package com.poly.app.domain.model;

import com.poly.app.domain.model.base.PrimaryEntity;
import com.poly.app.infrastructure.constant.DiscountType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "promotion")
// đợt giảm giá
public class Promotion extends PrimaryEntity implements Serializable {
    Integer id;
    // mã giảm
    String promotionCode;
    // tên loại giảm
    String promotionName;
    // loại giảm
    String promotionType;
    // giá trị giảm
    Double discountValue;
    @Enumerated(EnumType.STRING)
    DiscountType discountType;
    Integer quantity;
    // ngày bắt đầu
    LocalDateTime startDate;
    // ngày kết thúc
    LocalDateTime endDate;
    // trạng thái
    @Enumerated(EnumType.STRING)
    StatusEnum statusPromotion;
}
