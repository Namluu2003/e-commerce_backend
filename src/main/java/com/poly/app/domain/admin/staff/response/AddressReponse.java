package com.poly.app.domain.admin.staff.response;

import com.poly.app.domain.model.Address;
import lombok.Data;

@Data
public class AddressReponse {
    private String provinceId;
    private String districtId;
    private String wardId;
    private Boolean isAddressDefault;
    private String specificAddress;

    public AddressReponse(Address address) {
        this.provinceId = address.getProvinceId();
        this.districtId = address.getDistrictId();
        this.wardId = address.getWardId();
        this.isAddressDefault = address.getIsAddressDefault();
        this.specificAddress = address.getSpecificAddress();
    }
}
