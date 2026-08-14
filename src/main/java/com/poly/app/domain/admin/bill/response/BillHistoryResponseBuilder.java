package com.poly.app.domain.admin.bill.response;

import com.poly.app.infrastructure.constant.BillStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillHistoryResponseBuilder  {

    String staffName;
    String customerName;
    String billCode;
    BillStatus status;
    String description;
    Long createdAt;
    String createdBy;

    
}
