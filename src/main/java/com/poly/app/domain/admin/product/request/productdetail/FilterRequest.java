package com.poly.app.domain.admin.product.request.productdetail;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterRequest {
    String productName;
    String brandName;
    String typeName;
    String colorName;
    String materialName;
    String sizeName;
    String soleName;
    String genderName;
    String status;
    String sortByQuantity;
    String sortByPrice;


}
