
package com.poly.app.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.poly.app.domain.model.base.PrimaryEntity;
import com.poly.app.infrastructure.constant.AccountStatus;
import com.poly.app.infrastructure.constant.EntityProperties;

import jakarta.persistence.CascadeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "staff") // Nhân viên
public class Staff extends PrimaryEntity implements Serializable, UserDetails {

    private String code;

    @Column(columnDefinition = EntityProperties.DEFINITION_NAME)
    private String fullName;

    private LocalDateTime dateBirth;

    @Column(unique = true)
    private String citizenId;

    @Column(length = EntityProperties.LENGTH_PHONE, unique = true)
    private String phoneNumber;

    @Column(length = EntityProperties.LENGTH_EMAIL)
    private String email;

    private Boolean gender;

    @Column(length = EntityProperties.LENGTH_PASSWORD)
    private String password;

    private String avatar;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference // tránh vòng lặp
    private List<Address> addresses;

    // Phân quyền theo role
    private AccountStatus status = AccountStatus.HOAT_DONG;

    public Integer getStatus() {
        return status.ordinal();
    }

    public void setStatus(Integer status) {
        this.status = AccountStatus.values()[status];
    }

    private LocalDateTime lastSeen; // 👈 Thêm trường này để kiểm tra online/offline

    public boolean isOnline(Staff user) {
        return user.getLastSeen() != null &&
                user.getLastSeen().isAfter(LocalDateTime.now().minusSeconds(60));
    }
    @Column
    private Boolean isOnline;

    private String tokenActiveAccount;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        log.info("getAuthorities");
        String roleName = Optional.ofNullable(role)
                .map(Role::getRoleName)
                .orElse("ROLE_STAFF");  // Sử dụng quyền mặc định nếu role là null
        log.warn("roleName: {}", roleName);
        authorities.add(new SimpleGrantedAuthority(roleName));
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
