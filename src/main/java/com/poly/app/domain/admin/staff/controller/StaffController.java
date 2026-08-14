package com.poly.app.domain.admin.staff.controller;

import com.poly.app.domain.admin.address.AddressRequest;
import com.poly.app.domain.admin.customer.response.AddressResponse;
import com.poly.app.domain.admin.staff.request.StaffRequest;
import com.poly.app.domain.admin.staff.response.StaffReponse;
import com.poly.app.domain.admin.staff.service.Impl.StaffServiceImpl;
import com.poly.app.domain.admin.staff.service.StaffService;
import com.poly.app.infrastructure.util.ExcelHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;
    @Autowired
    private StaffServiceImpl staffServiceImpl;

    @PostMapping("/add")
    public ResponseEntity<StaffReponse> createStaff(@RequestBody StaffRequest staffRequest) {
        StaffReponse staffReponse = staffService.createStaff(staffRequest);
        return ResponseEntity.ok(staffReponse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StaffReponse> updateStaff(@PathVariable Integer id, @RequestBody StaffRequest staffRequest) {
        StaffReponse staffReponse = staffService.updateStaff(id, staffRequest);
        return ResponseEntity.ok(staffReponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Integer id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<StaffReponse> getStaffById(@PathVariable Integer id) {
        StaffReponse staffReponse = staffService.getStaffById(id);
        return ResponseEntity.ok(staffReponse);
    }

    @GetMapping("/hienthi")
    public ResponseEntity<List<StaffReponse>> getAllStaffs() {
        List<StaffReponse> staffs = staffService.getAllStaff();
        return ResponseEntity.ok(staffs);
    }

    @GetMapping("/detail/email")
    public ResponseEntity<StaffReponse> getStaffByEmail(@RequestParam String email) {
        StaffReponse staffReponse = staffService.getStaffByEmail(email);
        return ResponseEntity.ok(staffReponse);
    }

    @PutMapping("/update-address/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable Integer addressId, @RequestBody AddressRequest addressRequest) {
        AddressResponse addressResponse = staffService.updateAddress(addressId, addressRequest);
        return ResponseEntity.ok(addressResponse);
    }

    @DeleteMapping("/delete-address/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer addressId) {
        staffService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add-address/{customerId}")
    public ResponseEntity<AddressResponse> addAddress(@PathVariable Integer customerId, @RequestBody AddressRequest addressRequest) {
        AddressResponse addressResponse = staffService.addAddress(customerId, addressRequest);
        return ResponseEntity.ok(addressResponse);
    }

    @PutMapping("/set-default-address/{addressId}")
    public ResponseEntity<Void> setDefaultAddress(@PathVariable Integer addressId) {
        staffService.setDefaultAddress(addressId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/filterr")
    public ResponseEntity<List<StaffReponse>> filterStaff(
            @RequestParam(required = false) String searchText,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String dobFrom,
            @RequestParam(required = false) String dobTo,
            @RequestParam(required = false) Integer ageFrom,
            @RequestParam(required = false) Integer ageTo) {
        List<StaffReponse> filteredStaffs = staffService.filterStaff(searchText, status, dobFrom, dobTo, ageFrom, ageTo);
        return ResponseEntity.ok(filteredStaffs);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean exists = staffServiceImpl.checkEmailExists(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        HttpStatus status = exists ? HttpStatus.CONFLICT : HttpStatus.OK; // Or another appropriate status
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/check-phone")
    public ResponseEntity<Map<String, Boolean>> checkPhone(@RequestParam String phoneNumber) {
        boolean exists = staffServiceImpl.checkPhoneExists(phoneNumber);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        HttpStatus status = exists ? HttpStatus.CONFLICT : HttpStatus.OK;
        return new ResponseEntity<>(response, status);
    }


    @PostMapping("/import-exel")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) throws IOException {
        if (ExcelHelper.hasExcelFormat(file)) {
            staffService.saveEmployeesFromExcel(file);
            return ResponseEntity.ok("Upload thành công!");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File không đúng định dạng Excel");
    }

    @PutMapping("/ping")
    public ResponseEntity<?> ping(HttpServletRequest request) {

        staffService.updateLastSeen();
        return ResponseEntity.ok().build();
    }
    @GetMapping("/logout-status/{id}")
    public ResponseEntity<?> logout(@PathVariable("id") Integer id) {
        staffService.logoutStatus(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/login-status/{id}")   
    public ResponseEntity<?> login(@PathVariable("id") Integer id) {
        staffService.loginStatus(id);
        return ResponseEntity.ok().build();
    }
}







