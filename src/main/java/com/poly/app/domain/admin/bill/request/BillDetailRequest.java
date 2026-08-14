package com.poly.app.domain.admin.bill.request;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillDetailRequest {

    private Integer productDetailId;
    private Integer quantity;
    private Double price;
    private String image;

}
