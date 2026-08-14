package com.poly.app.domain.admin.bill.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PriceUpdateMessage {

    private Integer productId;
    private double newPrice;

}
