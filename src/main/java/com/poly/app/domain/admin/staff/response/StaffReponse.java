package com.poly.app.domain.admin.staff.response;

import com.poly.app.domain.model.Address;
import com.poly.app.domain.model.Customer;
import com.poly.app.domain.model.Staff;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffReponse {
    Integer id;
    String code;
    String fullName;
    LocalDateTime dateBirth;
    String CitizenId;
    String phoneNumber;
    String email;
    Boolean gender;
    String password;
     Integer status;
    String avatar;
    private List<Address> addresses;
    String roleName;

    public StaffReponse(Staff staff) {
        this.fullName = staff.getFullName();
        this.CitizenId = staff.getCitizenId();
        this.email = staff.getEmail();
        this.gender = staff.getGender();
        this.phoneNumber = staff.getPhoneNumber();
        this.password = staff.getPassword();
        this.dateBirth = staff.getDateBirth();
        this.avatar = staff.getAvatar();
        this.addresses = staff.getAddresses();
        this.id = staff.getId();
        this.status = staff.getStatus();
        this.roleName = staff.getRole() != null ? staff.getRole().getRoleName() : "ROLE_STAFF";
    }
}