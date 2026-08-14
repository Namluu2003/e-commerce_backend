package com.poly.app.domain.auth.controller;


import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import com.poly.app.domain.auth.request.ChangeRequest;
import com.poly.app.domain.auth.request.LoginRequest;
import com.poly.app.domain.auth.request.RegisterRequest;
import com.poly.app.domain.auth.request.ResetPasswordRequest;
import com.poly.app.domain.auth.service.AuthenticationService;
import com.poly.app.domain.common.ObjectResponse;
import com.poly.app.domain.model.Customer;
import com.poly.app.infrastructure.security.Auth;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    HttpSession session;

    @Autowired
    Auth auth;


    @PostMapping("/register")
    public ObjectResponse  register(@Valid @RequestBody RegisterRequest request) {
        return new ObjectResponse(authenticationService.register(request));
    }

    @PostMapping("/admin-register")
    public ObjectResponse  staffRegister(@Valid @RequestBody RegisterRequest request) {
        return new ObjectResponse(authenticationService.registerStaff(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangeRequest request) {
        authenticationService.changePassword(request);
        return ResponseEntity.ok("Password changed");
    }


    @PostMapping("/login-admin")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.loginAdmin(request));
    }


    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo() {
        Customer customer = auth.getCustomerAuth();

        return ResponseEntity.ok(customer);
    }

    @GetMapping("/forgot-password/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable String email) {
        authenticationService.forgotPassword(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-admin-password/{email}")
    public ResponseEntity<?> forgotAdminPassword(@PathVariable String email) {
        authenticationService.forgotAdminPassword(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        authenticationService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/reset-admin-password")
    public ResponseEntity<?> resetAdminPassword(@RequestBody ResetPasswordRequest request) {
        authenticationService.resetAdminPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(String request) {
//        String token = jwtUtilities.getToken(request);
//        if (token != null) {
//            // Lấy thông tin người dùng từ token
//            String username = jwtUtilities.extractUsername(token);
//            // Hủy token bằng cách thêm vào danh sách đen
//            tokenBlacklistService.addTokenToBlacklist(token);
//            // Thoát phiên
//            httpSession.invalidate();
//            // Ghi log hoặc thực hiện hành động khác
//            System.out.println("User " + username + " has logged out.");
//        }

        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/active")
    public ResponseEntity<String> activateAccount(@RequestParam String token) {
        authenticationService.activateAccount(token);
        return ResponseEntity.ok("Active account successfully");
    }


    // Endpoint để xử lý đăng nhập Google
    @PostMapping("/google-login/{token}")
    public ResponseEntity<?> googleLogin(@PathVariable String token) {
        return ResponseEntity.ok(authenticationService.loginGoogle(token));
    }
}
