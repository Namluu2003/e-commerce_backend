package com.poly.app.domain.auth.response;

import com.poly.app.domain.model.Address;
import com.poly.app.domain.model.Customer;
import com.poly.app.domain.model.Staff;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class UserLoginResponse  {
    private Integer id;
    private String fullName;
    private String CitizenId;
    private String email;
    private Boolean gender;
    private String phoneNumber;
    private String password;
    private String avatar;
    private Integer status;
    private LocalDateTime dateBirth;
    private List<Address> addresses;
    private String role;


    public static UserLoginResponse fromCustomerEntity(Customer customer) {
        return  UserLoginResponse
                .builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .CitizenId(customer.getCitizenId())
                .email(customer.getEmail())
                .gender(customer.getGender())
                .phoneNumber(customer.getPhoneNumber())
                .avatar(customer.getAvatar())
                .role("CUSTOMER")
                .build();
    }

    public static UserLoginResponse fromStaffEntity(Staff staff) {
        return  UserLoginResponse
                .builder()
                .id(staff.getId())
                .fullName(staff.getFullName())
                .CitizenId(staff.getCitizenId())
                .email(staff.getEmail())
                .gender(staff.getGender())
                .phoneNumber(staff.getPhoneNumber())
                .avatar(staff.getAvatar())
                .role(staff.getRole().getRoleName())
                .build();
    }


}
