
package com.poly.app.domain.admin.customer.service;

import com.poly.app.domain.admin.customer.request.CustomerRequest;
import com.poly.app.domain.admin.customer.response.CustomerResponse;

import com.poly.app.domain.admin.address.AddressRequest;
import com.poly.app.domain.admin.customer.response.AddressResponse;

import com.poly.app.domain.admin.address.AddressRequest;
import com.poly.app.domain.admin.customer.response.AddressResponse;
import com.poly.app.domain.common.PageReponse;
import com.poly.app.domain.model.Customer;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest customerRequest);

    CustomerResponse updateCustomer(Integer id, CustomerRequest customerRequest);

    void deleteCustomer(Integer id);

    CustomerResponse getCustomerById(Integer id);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse getCustomerByEmail(String email);
    Customer getEntityCustomerByEmail(String email);


    AddressResponse updateAddress(Integer addressId, AddressRequest addressRequest);

    void deleteAddress(Integer addressId);

    AddressResponse addAddress(Integer customerId, AddressRequest addressRequest);

    void setDefaultAddress(Integer addressId);

    List<CustomerResponse> filterCustomers(String searchText, String status, LocalDateTime startDate, LocalDateTime endDate, Integer minAge, Integer maxAge);

    boolean checkEmailExists(String email);
    boolean checkPhoneExists(String phoneNumber);

    List<CustomerResponse> findCustomerByQuery(String query);

    Page<CustomerResponse> filterAndSearchCustomers(String searchText, String status, LocalDateTime startDate, LocalDateTime endDate, Integer minAge, Integer maxAge, Integer size, Integer page);
}