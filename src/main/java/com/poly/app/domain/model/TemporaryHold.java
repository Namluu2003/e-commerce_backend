package com.poly.app.domain.model;

import com.poly.app.domain.model.base.PrimaryEntity;
import com.poly.app.infrastructure.constant.HoldStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "temporary_holds")
@Data
public class TemporaryHold extends PrimaryEntity implements Serializable {


    private String productId;
    private Integer quantity;
    private String billId;
    private LocalDateTime holdTime;
    private LocalDateTime expireTime;

    @Enumerated(EnumType.STRING)
    private HoldStatus status; // HOLDING, RELEASED, CONFIRMED

    // Constructor để tạo hold mới
    public static TemporaryHold createNew(String productId, Integer quantity, String billId) {
        TemporaryHold hold = new TemporaryHold();
        hold.setProductId(productId);
        hold.setQuantity(quantity);
        hold.setBillId(billId);
        hold.setHoldTime(LocalDateTime.now());
        hold.setExpireTime(LocalDateTime.now().plusMinutes(15));
        hold.setStatus(HoldStatus.HOLDING);
        return hold;
    }
}