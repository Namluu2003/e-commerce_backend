package com.poly.app.domain.admin.freeship_order.request;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class FreeshipOrderRequest {

    @NotNull(message = "Giá trị đơn hàng tối thiểu không được để trống")
    @Min(value = 0, message = "Giá trị đơn hàng tối thiểu phải lớn hơn hoặc bằng 0")
    private Double minOrderValue;

}
