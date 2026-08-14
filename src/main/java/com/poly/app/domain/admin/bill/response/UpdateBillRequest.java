package com.poly.app.domain.admin.bill.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateBillRequest {

    private String billCode;
    private String status;
    private String type;
    private String customerName;
    private String email;
    private String customerPhone;
    private String shippingAddress;
    private String note;
    private Double feeShipping;

    String provinceId; // tỉnh
    String districtId; // quận
    String wardId; // xã
    String specificAddress;




}
