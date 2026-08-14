package com.poly.app.domain.admin.auth;

import com.poly.app.domain.auth.request.ChangePasswordRequest;
import com.poly.app.domain.auth.service.AuthenticationService;
import com.poly.app.domain.model.Customer;
import com.poly.app.domain.model.Staff;
import com.poly.app.domain.repository.CustomerRepository;
import com.poly.app.domain.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/auth")
public class AuthUser {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerRepository customerRepository;

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            Customer customer = authenticationService.getCustomerAuth();

            // Kiểm tra mật khẩu cũ
            if (!passwordEncoder.matches(request.getOldPassword(), customer.getPassword())) {
                return ResponseEntity.badRequest().body("Mật khẩu cũ không đúng");
            }

            // Mã hóa mật khẩu mới và lưu lại
            customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
            customerRepository.save(customer);

            return ResponseEntity.ok("Đổi mật khẩu thành công");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra khi đổi mật khẩu");
        }
    }



}
