package com.poly.app.domain.client.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddCart {
    Integer customerId;
    Integer productDetailId;
    String productName;
    Double price;
    Integer quantityAddCart;
    String image;

}
