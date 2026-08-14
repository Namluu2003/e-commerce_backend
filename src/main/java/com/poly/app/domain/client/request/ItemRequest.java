package com.poly.app.domain.client.request;

import com.poly.app.domain.admin.address.AddressRequest;
import com.poly.app.domain.admin.bill.request.BillDetailRequest;
import com.poly.app.infrastructure.constant.PaymentMethodsType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest {
    private String itemid;
    private String itemname;
    private long itemprice;
    private int itemquantity;

}
