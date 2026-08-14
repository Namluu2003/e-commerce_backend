
package com.poly.app.domain.admin.address;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressRequest {
    private String provinceId;
    private String districtId;
    private String wardId;
    private Boolean isAddressDefault;
    private String specificAddress;
}