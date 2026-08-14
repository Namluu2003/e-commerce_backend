//
//package com.poly.app.domain.admin.customer.controller;
//
//
//import com.poly.app.domain.admin.address.AddressRequest;
//import com.poly.app.domain.admin.customer.request.CustomerRequest;
//import com.poly.app.domain.admin.customer.response.AddressResponse;
//import com.poly.app.domain.admin.customer.response.CustomerResponse;
//import com.poly.app.domain.admin.customer.service.CustomerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/customers")
//public class CustomerController {
//
//    @Autowired
//    private CustomerService customerService;
//
//
//    @PostMapping("/add")
//    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest customerRequest) {
//        CustomerResponse customerResponse = customerService.createCustomer(customerRequest);
//        return ResponseEntity.ok(customerResponse);
//    }
//
//    @PutMapping("update/{id}")
//    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Integer id, @RequestBody CustomerRequest customerRequest) {
//        CustomerResponse customerResponse = customerService.updateCustomer(id, customerRequest);
//        return ResponseEntity.ok(customerResponse);
//    }
//
//    @DeleteMapping("delete/{id}")
//    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
//        customerService.deleteCustomer(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("detail/{id}")
//    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Integer id) {
//        CustomerResponse customerResponse = customerService.getCustomerById(id);
//        return ResponseEntity.ok(customerResponse);
//    }
//
//    @GetMapping("/")
//    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
//        List<CustomerResponse> customers = customerService.getAllCustomers();
//        return ResponseEntity.ok(customers);
//    }
//
//    @GetMapping("detail/email")
//    public ResponseEntity<CustomerResponse> getCustomerByEmail(@RequestParam String email) {
//        CustomerResponse customerResponse = customerService.getCustomerByEmail(email);
//        return ResponseEntity.ok(customerResponse);
//    }
//
//
//    @PutMapping("/update-address/{addressId}")
//    public ResponseEntity<AddressResponse> updateAddress(@PathVariable Integer addressId, @RequestBody AddressRequest addressRequest) {
//        AddressResponse addressResponse = customerService.updateAddress(addressId, addressRequest);
//        return ResponseEntity.ok(addressResponse);
//    }
//
//    @DeleteMapping("/delete-address/{addressId}")
//    public ResponseEntity<Void> deleteAddress(@PathVariable Integer addressId) {
//        customerService.deleteAddress(addressId);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/add-address/{customerId}")
//    public ResponseEntity<AddressResponse> addAddress(@PathVariable Integer customerId, @RequestBody AddressRequest addressRequest) {
//        AddressResponse addressResponse = customerService.addAddress(customerId, addressRequest);
//        return ResponseEntity.ok(addressResponse);
//    }
//
//    @PutMapping("/set-default-address/{addressId}")
//    public ResponseEntity<Void> setDefaultAddress(@PathVariable Integer addressId) {
//        customerService.setDefaultAddress(addressId);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/filter")
//    public ResponseEntity<List<CustomerResponse>> filterCustomers(
//            @RequestParam String searchText,
//            @RequestParam String status,
//            @RequestParam(required = false) LocalDateTime startDate,
//            @RequestParam(required = false) LocalDateTime endDate,
//            @RequestParam Integer minAge,
//            @RequestParam Integer maxAge) {
//        List<CustomerResponse> customers = customerService.filterCustomers(searchText, status, startDate, endDate, minAge, maxAge);
//        return ResponseEntity.ok(customers);
//    }
//}

package com.poly.app.domain.admin.customer.controller;
import com.poly.app.domain.admin.address.AddressRequest;
import com.poly.app.domain.admin.customer.request.CustomerRequest;
import com.poly.app.domain.admin.customer.response.AddressResponse;
import com.poly.app.domain.admin.customer.response.CustomerResponse;
import com.poly.app.domain.admin.customer.service.CustomerService;
import com.poly.app.domain.admin.customer.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerServiceImpl customerServiceImpl;

    @PostMapping("/add")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerRequest customerRequest) {
        try {
            CustomerResponse customerResponse = customerService.createCustomer(customerRequest);
            return ResponseEntity.ok(customerResponse);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Integer id, @RequestBody CustomerRequest customerRequest) {
        CustomerResponse customerResponse = customerService.updateCustomer(id, customerRequest);
        return ResponseEntity.ok(customerResponse);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("detail/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Integer id) {
        CustomerResponse customerResponse = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerResponse);
    }

    @GetMapping("/")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("detail/email")
    public ResponseEntity<CustomerResponse> getCustomerByEmail(@RequestParam String email) {
        CustomerResponse customerResponse = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok(customerResponse);
    }


    @PutMapping("/update-address/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable Integer addressId, @RequestBody AddressRequest addressRequest) {
        AddressResponse addressResponse = customerService.updateAddress(addressId, addressRequest);
        return ResponseEntity.ok(addressResponse);
    }

    @DeleteMapping("/delete-address/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer addressId) {
        customerService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add-address/{customerId}")
    public ResponseEntity<AddressResponse> addAddress(@PathVariable Integer customerId, @RequestBody AddressRequest addressRequest) {
        AddressResponse addressResponse = customerService.addAddress(customerId, addressRequest);
        return ResponseEntity.ok(addressResponse);
    }

    @PutMapping("/set-default-address/{addressId}")
    public ResponseEntity<Void> setDefaultAddress(@PathVariable Integer addressId) {
        customerService.setDefaultAddress(addressId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<CustomerResponse>> filterCustomers(
            @RequestParam String searchText,
            @RequestParam String status,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam Integer minAge,
            @RequestParam Integer maxAge) {
        List<CustomerResponse> customers = customerService.filterCustomers(searchText, status, startDate, endDate, minAge, maxAge);
        return ResponseEntity.ok(customers);
    }


    @GetMapping("/index")
    public ResponseEntity<?> filterCustomers(
            @RequestParam(required = false) String searchText,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "0") Integer page
    ) {

        return ResponseEntity.ok(customerService
                .filterAndSearchCustomers(searchText, status, startDate, endDate, minAge, maxAge, size, page));
    }



    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean exists = customerServiceImpl.checkEmailExists(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        HttpStatus status = exists ? HttpStatus.CONFLICT : HttpStatus.OK; // Or another appropriate status
        return new ResponseEntity<>(response, status);
    }
    @GetMapping("/check-phone")
    public ResponseEntity<Map<String, Boolean>> checkPhone(@RequestParam String phoneNumber) {
        boolean exists = customerServiceImpl.checkPhoneExists(phoneNumber);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        HttpStatus status = exists ? HttpStatus.CONFLICT : HttpStatus.OK;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("search")
    public ResponseEntity<List<CustomerResponse>> getCustomers(
            @RequestParam String query
    ) {
      return ResponseEntity.ok(customerService.findCustomerByQuery(query));
    }


}