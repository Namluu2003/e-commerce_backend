
package com.poly.app.domain.admin.customer.response;
import com.poly.app.domain.model.Address;
import com.poly.app.domain.model.Customer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CustomerResponse {
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
    // List of addresses
    private List<Address> addresses;

    public CustomerResponse(Customer customer) {
        this.id = customer.getId();
        this.fullName = customer.getFullName();
        this.CitizenId = customer.getCitizenId();
        this.email = customer.getEmail();
        this.gender = customer.getGender();
        this.phoneNumber = customer.getPhoneNumber();
        this.password = customer.getPassword();
        this.dateBirth = customer.getDateBirth();
        this.avatar = customer.getAvatar();
        this.status = customer.getStatus();
        this.addresses = customer.getAddresses();
    }

}