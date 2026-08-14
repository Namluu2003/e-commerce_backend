package com.poly.app.domain.admin.bill.response;


import com.poly.app.domain.model.base.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillProductDetailResponse  {
    Integer ProductDetailId;
    String urlImage;
    Integer quantity;
    String productName;
    Double price;
    String size;
    String color;
    Double totalPrice;
    Long createdAt;

    String sizeName;
    String colorName;
    String materialName;
    String soleName;


}
