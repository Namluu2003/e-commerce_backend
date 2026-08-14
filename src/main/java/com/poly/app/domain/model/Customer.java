package com.poly.app.domain.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.poly.app.domain.model.base.PrimaryEntity;
import com.poly.app.infrastructure.constant.AccountStatus;
import com.poly.app.infrastructure.constant.EntityProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "customer")
public class Customer extends PrimaryEntity implements Serializable, UserDetails {

    private String code;

    @Column( columnDefinition = EntityProperties.DEFINITION_NAME)
    private String fullName;


    private LocalDateTime dateBirth;

    @Column(unique = true)
    private String CitizenId;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference // tránh vòng lặp
    private List<Address> addresses;

    @Column(length = EntityProperties.LENGTH_PHONE)
    private String phoneNumber;

    @Column(length = EntityProperties.LENGTH_EMAIL)
    private String email;

    private Boolean gender;

    @Column(length = EntityProperties.LENGTH_PASSWORD)
    private String password;

    private String avatar;

    private AccountStatus status = AccountStatus.CHUA_KICH_HOAT;

    public Integer getStatus() {
        return status.ordinal();
    }

    public void setStatus(Integer status) {
        this.status = AccountStatus.values()[status];
    }


    private String tokenActiveAccount;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("getAuthorities");
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
