package com.poly.app.domain.client.response;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collector;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillDetailResponse {

    private Integer productDetailId;
    private Integer quantity;
    private Double price;
    private String image;
    private String productDetailname;
    private Integer productId;
    private Integer colorId;
    private Integer sizeId;


}
