package com.poly.app.domain.admin.staff.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffRequest {
    private String code;
    private String fullName;
    private Integer status;
    private LocalDateTime dateBirth;
    private String CitizenId;
    private String phoneNumber;
    private String email;
    private Boolean gender;
    private String password;
    private String avatar;
    private String provinceId;
    private String districtId;
    private String wardId;
    private Boolean isAddressDefault;
    private String specificAddress;
    private String roleName;
}