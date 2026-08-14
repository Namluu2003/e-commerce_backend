package com.poly.app.domain.admin.bill.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BillProductDetailRequest {

    Integer id;
    Integer productDetailId;
    Integer quantity;

    Double price;

}
