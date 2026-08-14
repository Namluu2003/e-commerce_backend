

package com.poly.app.domain.admin.customer.request;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerRequest {
    private String fullName;
    private String CitizenId;
    private String email;
    private Boolean gender;
    private String phoneNumber;
    private LocalDateTime dateBirth;
    private String password;
    private String avatar;
    private Integer status;
    private String provinceId;
    private String districtId;
    private String wardId;
    private Boolean isAddressDefault;
    private String specificAddress;


}
