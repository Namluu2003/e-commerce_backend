package com.poly.app.domain.client.response;

import com.poly.app.domain.model.Voucher;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherBestResponse {
   Voucher voucher;
   Double discount;
   String note;
}
