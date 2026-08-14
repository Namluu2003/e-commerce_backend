//
//package com.poly.app.domain.admin.customer.service.impl;
//
//
//import com.poly.app.domain.admin.address.AddressRequest;
//import com.poly.app.domain.admin.customer.request.CustomerRequest;
//import com.poly.app.domain.admin.customer.response.AddressResponse;
//import com.poly.app.domain.admin.customer.response.CustomerResponse;
//
//import com.poly.app.domain.admin.customer.service.CustomerService;
//
//import com.poly.app.domain.model.Address;
//import com.poly.app.domain.model.Customer;
//import com.poly.app.domain.repository.AddressRepository;
//import com.poly.app.domain.repository.CustomerRepository;
//import com.poly.app.infrastructure.email.Email;
//import com.poly.app.infrastructure.email.EmailSender;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class CustomerServiceImpl implements CustomerService {
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    private AddressRepository addressRepository;
//
//    @Autowired
//    private EmailSender emailSender;
//
//    @Override
//    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
//        Customer customer = new Customer();
//        customer.setFullName(customerRequest.getFullName());
//        customer.setEmail(customerRequest.getEmail());
//        customer.setPhoneNumber(customerRequest.getPhoneNumber());
//        customer.setDateBirth(customerRequest.getDateBirth());
//        customer.setPassword(customerRequest.getPassword());
//        customer.setCitizenId(customerRequest.getCitizenId());
//        customer.setGender(customerRequest.getGender());
//        customer.setAvatar(customerRequest.getAvatar());
//        customer.setStatus(customerRequest.getStatus());
//        customerRepository.save(customer);
//        Address address = Address.builder()
//                .provinceId(customerRequest.getProvinceId())
//                .districtId(customerRequest.getDistrictId())
//                .wardId(customerRequest.getWardId())
//                .specificAddress(customerRequest.getSpecificAddress())
//                .build();
//        address.setCustomer(customer);
//        address.setIsAddressDefault(true);
//        addressRepository.save(address);
//
//        Customer customerFromDB = customerRepository.findById(customer.getId()).orElse(null);
//        assert customerFromDB != null;
//        Email email = new Email();
//        String[] emailSend = {customerRequest.getEmail()};
//        email.setToEmail(emailSend);
//        email.setSubject("Tạo tài khoản thành công");
//        email.setTitleEmail("");
//        email.setBody("<!DOCTYPE html>\n" +
//                "<html lang=\"en\">\n" +
//                "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; margin: 50px;\">\n" +
//                "\n" +
//                "    <div class=\"success-message\" style=\"background-color: #FFFFF; color: black; padding: 20px; border-radius: 10px; margin-top: 50px;\">\n" +
//                "        <h2 style=\"color: #333;\">Tài khoản đã được tạo thành công!</h2>\n" +
//                "        <p style=\"color: #555;\">Cảm ơn bạn đã đăng ký tại TheHands. Dưới đây là thông tin đăng nhập của bạn:</p>\n" +
//                "        <p><strong>Email:</strong> " + customerRequest.getEmail() + "</p>\n" +
//                "        <p><strong>Mật khẩu:</strong> " + customerRequest.getPassword() + "</p>\n" +
//                "        <p style=\"color: #555;\">Đăng nhập ngay để trải nghiệm!</p>\n" +
//                "    </div>\n" +
//                "\n" +
//                "</body>\n" +
//                "</html>\n");
//
//
//        emailSender.sendEmail(email);
//        return new CustomerResponse(customerFromDB);
//    }
//
//    @Override
//    public CustomerResponse updateCustomer(Integer id, CustomerRequest customerRequest) {
//        Optional<Customer> optionalCustomer = customerRepository.findById(id);
//        if (optionalCustomer.isPresent()) {
//            Customer customer = optionalCustomer.get();
//            // Update fields
//            customer.setFullName(customerRequest.getFullName());
//            customer.setEmail(customerRequest.getEmail());
//            customer.setPhoneNumber(customerRequest.getPhoneNumber());
//            customer.setDateBirth(customerRequest.getDateBirth());
//            customer.setPassword(customerRequest.getPassword());
//            customer.setCitizenId(customerRequest.getCitizenId());
//            customer.setGender(customerRequest.getGender());
//            customer.setAvatar(customerRequest.getAvatar());
//            customer.setStatus(customerRequest.getStatus());
//
//            // Clear existing addresses
//            customer.getAddresses().clear();
//
//
//            Customer updatedCustomer = customerRepository.save(customer);
//            return new CustomerResponse(updatedCustomer);
//        } else {
//            throw new RuntimeException("Customer not found");
//        }
//    }
//
//    @Override
//    public void deleteCustomer(Integer id) {
//        customerRepository.deleteById(id);
//    }
//
//    @Override
//    public CustomerResponse getCustomerById(Integer id) {
//        Optional<Customer> optionalCustomer = customerRepository.findById(id);
//        return optionalCustomer.map(CustomerResponse::new).orElseThrow(() -> new RuntimeException("Customer not found"));
//    }
//
//    @Override
//    public List<CustomerResponse> getAllCustomers() {
//        List<Customer> customers = customerRepository.findAll();
//        Collections.reverse(customers);
//        return customers.stream().map(CustomerResponse::new).collect(Collectors.toList());
//    }
//
//    @Override
//    public CustomerResponse getCustomerByEmail(String email) {
//        Optional<Customer> optionalCustomer = Optional.ofNullable(customerRepository.findByEmail(email));
//        return optionalCustomer.map(CustomerResponse::new).orElseThrow(() -> new RuntimeException("Customer not found"));
//    }
//
//
//    @Override
//    public AddressResponse updateAddress(Integer addressId, AddressRequest addressRequest) {
//        Optional<Address> optionalAddress = addressRepository.findById(addressId);
//        if (optionalAddress.isPresent()) {
//            Address address = optionalAddress.get();
//            address.setProvinceId(addressRequest.getProvinceId());
//            address.setDistrictId(addressRequest.getDistrictId());
//            address.setWardId(addressRequest.getWardId());
//            address.setSpecificAddress(addressRequest.getSpecificAddress());
//            address.setIsAddressDefault(addressRequest.getIsAddressDefault());
//            Address updatedAddress = addressRepository.save(address);
//            return new AddressResponse(updatedAddress);
//        } else {
//            throw new RuntimeException("Address not found");
//        }
//    }
//
//    @Override
//    public void deleteAddress(Integer addressId) {
//
//        addressRepository.deleteById(addressId);
//    }
//
//
//
//    @Override
//    public AddressResponse addAddress(Integer customerId, AddressRequest addressRequest) {
//        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
//        if (optionalCustomer.isPresent()) {
//            Customer customer = optionalCustomer.get();
//            Address address = Address.builder()
//                    .provinceId(addressRequest.getProvinceId())
//                    .districtId(addressRequest.getDistrictId())
//                    .wardId(addressRequest.getWardId())
//                    .specificAddress(addressRequest.getSpecificAddress())
//                    .isAddressDefault(addressRequest.getIsAddressDefault() != null ? addressRequest.getIsAddressDefault() : false)
//                    .build();
//            address.setCustomer(customer);
//            Address savedAddress = addressRepository.save(address);
//            return new AddressResponse(savedAddress);
//        } else {
//            throw new RuntimeException("Customer not found");
//        }
//    }
//
//
//
//
//    @Override
//    public void setDefaultAddress(Integer addressId) {
//        Optional<Address> optionalAddress = addressRepository.findById(addressId);
//        if (optionalAddress.isPresent()) {
//            Address address = optionalAddress.get();
//
//            // Đặt tất cả địa chỉ về không phải mặc định
//            List<Address> addresses = addressRepository.findByCustomerId(address.getCustomer().getId());
//            addresses.forEach(addr -> {
//                if (Boolean.TRUE.equals(addr.getIsAddressDefault())) {
//                    addr.setIsAddressDefault(false);
//                    addressRepository.save(addr);
//                }
//            });
//
//            // Đặt địa chỉ hiện tại làm mặc định
//            address.setIsAddressDefault(true);
//            addressRepository.save(address);
//        } else {
//            throw new RuntimeException("Address not found");
//        }
//    }
//
//
//
//
//    @Override
//    public List<CustomerResponse> filterCustomers(String searchText, String status, LocalDateTime startDate, LocalDateTime endDate, Integer minAge, Integer maxAge) {
//        List<Customer> customers = customerRepository.findAll();
//        customers = customers.stream()
//                .filter(customer -> customer.getFullName().toLowerCase().contains(searchText.toLowerCase()) ||
//                        customer.getPhoneNumber().contains(searchText))
//                .filter(customer -> status.equals("Tất cả") ||
//                        (status.equals("Kích hoạt") && customer.getStatus() == 1) ||
//                        (status.equals("Khóa") && customer.getStatus() == 0))
//                .filter(customer -> {
//                    if (startDate != null && endDate != null) {
//                        return !customer.getDateBirth().isBefore(startDate) && !customer.getDateBirth().isAfter(endDate);
//                    }
//                    return true;
//                })
//                .filter(customer -> {
//                    int age = LocalDateTime.now().getYear() - customer.getDateBirth().getYear();
//                    return age >= minAge && age <= maxAge;
//                })
//                .collect(Collectors.toList());
//
//        return customers.stream().map(CustomerResponse::new).collect(Collectors.toList());
//    }
//
//
//}






package com.poly.app.domain.admin.customer.service.impl;

import com.poly.app.domain.admin.address.AddressRequest;
import com.poly.app.domain.admin.customer.request.CustomerRequest;
import com.poly.app.domain.admin.customer.response.AddressResponse;
import com.poly.app.domain.admin.customer.response.CustomerResponse;

import com.poly.app.domain.admin.customer.service.CustomerService;

import com.poly.app.domain.common.PageReponse;
import com.poly.app.domain.model.Address;
import com.poly.app.domain.model.Customer;
import com.poly.app.domain.repository.AddressRepository;
import com.poly.app.domain.repository.CustomerRepository;
import com.poly.app.domain.repository.StaffRepository;
import com.poly.app.infrastructure.constant.AccountStatus;
import com.poly.app.infrastructure.email.Email;
import com.poly.app.infrastructure.email.EmailSender;
import com.poly.app.infrastructure.exception.RestApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EmailSender emailSender;
    @Autowired
    private StaffRepository staffRepository;

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        if (customerRepository.findByEmail(customerRequest.getEmail()) != null) {
            throw new RestApiException( "Email đã tồn tại", HttpStatus.BAD_REQUEST);
        }
        if(customerRequest.getPhoneNumber() != null ) {
            if (customerRepository.existsCustomersByPhoneNumber(customerRequest.getPhoneNumber())) {
                throw new RestApiException( "Số điện thoại đã tồn tại", HttpStatus.BAD_REQUEST);
            }
        }
        if (staffRepository.findByEmail(customerRequest.getEmail()) != null) {
            throw new RestApiException( "Email đã tồn tại", HttpStatus.BAD_REQUEST);
        }
        Customer customer = new Customer();
        customer.setFullName(customerRequest.getFullName());
        customer.setEmail(customerRequest.getEmail());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());
        customer.setDateBirth(customerRequest.getDateBirth());
        customer.setPassword(customerRequest.getPassword());
        customer.setCitizenId(customerRequest.getCitizenId());
        customer.setGender(customerRequest.getGender());
        customer.setAvatar(customerRequest.getAvatar());
        customer.setStatus(0);
        customerRepository.save(customer);
        if (customerRequest.getProvinceId() != null && customerRequest.getDistrictId() != null && customerRequest.getWardId() != null) {
            Address address = Address.builder()
                    .provinceId(customerRequest.getProvinceId())
                    .districtId(customerRequest.getDistrictId())
                    .wardId(customerRequest.getWardId())
                    .specificAddress(customerRequest.getSpecificAddress())
                    .build();
            address.setCustomer(customer);
            address.setIsAddressDefault(true);
            addressRepository.save(address);
        }


        Customer customerFromDB = customerRepository.findById(customer.getId()).orElse(null);
        assert customerFromDB != null;
        Email email = new Email();
        String[] emailSend = {customerRequest.getEmail()};
        email.setToEmail(emailSend);
        email.setSubject("Tạo tài khoản thành công");
        email.setTitleEmail("");
        email.setBody("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; margin: 50px;\">\n" +
                "\n" +
                "    <div class=\"success-message\" style=\"background-color: #FFFFF; color: black; padding: 20px; border-radius: 10px; margin-top: 50px;\">\n" +
                "        <h2 style=\"color: #333;\">Tài khoản đã được tạo thành công!</h2>\n" +
                "        <p style=\"color: #555;\">Cảm ơn bạn đã đăng ký tại TheHands. Dưới đây là thông tin đăng nhập của bạn:</p>\n" +
                "        <p><strong>Email:</strong> " + customerRequest.getEmail() + "</p>\n" +
                "        <p><strong>Mật khẩu:</strong> " + customerRequest.getPassword() + "</p>\n" +
                "        <p style=\"color: #555;\">Đăng nhập ngay để trải nghiệm!</p>\n" +
                "    </div>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n");


        emailSender.sendEmail(email);
        return new CustomerResponse(customerFromDB);
    }


    @Override
    public CustomerResponse updateCustomer(Integer id, CustomerRequest customerRequest) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        // Cập nhật thông tin khách hàng
        customer.setFullName(customerRequest.getFullName());
        customer.setEmail(customerRequest.getEmail());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());
        customer.setDateBirth(customerRequest.getDateBirth());
//        customer.setPassword(customerRequest.getPassword());
        customer.setCitizenId(customerRequest.getCitizenId());
        customer.setGender(customerRequest.getGender());
        customer.setAvatar(customerRequest.getAvatar());
        customer.setStatus(customerRequest.getStatus());
        customerRepository.save(customer);

        // Tìm địa chỉ mặc định
        Address defaultAddress = addressRepository.findFirstByCustomerIdAndIsAddressDefault(id, true);

        // Cập nhật hoặc thêm địa chỉ mặc định
        if (defaultAddress != null) {
            defaultAddress.setProvinceId(customerRequest.getProvinceId());
            defaultAddress.setDistrictId(customerRequest.getDistrictId());
            defaultAddress.setWardId(customerRequest.getWardId());
            defaultAddress.setSpecificAddress(customerRequest.getSpecificAddress());
            addressRepository.save(defaultAddress);
        } else {
            Address newAddress = Address.builder()
                    .provinceId(customerRequest.getProvinceId())
                    .districtId(customerRequest.getDistrictId())
                    .wardId(customerRequest.getWardId())
                    .specificAddress(customerRequest.getSpecificAddress())
                    .isAddressDefault(true)
                    .customer(customer)
                    .build();
            addressRepository.save(newAddress);
        }

        return new CustomerResponse(customerRepository.findById(id).get());
    }

    @Override
    public void deleteCustomer(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    public CustomerResponse getCustomerById(Integer id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        return optionalCustomer.map(CustomerResponse::new).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        Collections.reverse(customers);
        return customers.stream().map(CustomerResponse::new).collect(Collectors.toList());
    }

    @Override
    public CustomerResponse getCustomerByEmail(String email) {
        Optional<Customer> optionalCustomer = Optional.ofNullable(customerRepository.findByEmail(email));
        return optionalCustomer.map(CustomerResponse::new).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public Customer  getEntityCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }


    @Override
    public AddressResponse updateAddress(Integer addressId, AddressRequest addressRequest) {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        if (optionalAddress.isPresent()) {
            Address address = optionalAddress.get();
            address.setProvinceId(addressRequest.getProvinceId());
            address.setDistrictId(addressRequest.getDistrictId());
            address.setWardId(addressRequest.getWardId());
            address.setSpecificAddress(addressRequest.getSpecificAddress());
            address.setIsAddressDefault(addressRequest.getIsAddressDefault());
            Address updatedAddress = addressRepository.save(address);
            return new AddressResponse(updatedAddress);
        } else {
            throw new RuntimeException("Address not found");
        }
    }

    @Override
    public void deleteAddress(Integer addressId) {

        addressRepository.deleteById(addressId);
    }



    @Override
    public AddressResponse addAddress(Integer customerId, AddressRequest addressRequest) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            Address address = Address.builder()
                    .provinceId(addressRequest.getProvinceId())
                    .districtId(addressRequest.getDistrictId())
                    .wardId(addressRequest.getWardId())
                    .specificAddress(addressRequest.getSpecificAddress())
                    .isAddressDefault(addressRequest.getIsAddressDefault() != null ? addressRequest.getIsAddressDefault() : false)
                    .build();
            address.setCustomer(customer);
            Address savedAddress = addressRepository.save(address);
            return new AddressResponse(savedAddress);
        } else {
            throw new RuntimeException("Customer not found");
        }
    }




    @Override
    public void setDefaultAddress(Integer addressId) {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        if (optionalAddress.isPresent()) {
            Address address = optionalAddress.get();

            // Đặt tất cả địa chỉ về không phải mặc định
            List<Address> addresses = addressRepository.findByCustomerId(address.getCustomer().getId());
            addresses.forEach(addr -> {
                if (Boolean.TRUE.equals(addr.getIsAddressDefault())) {
                    addr.setIsAddressDefault(false);
                    addressRepository.save(addr);
                }
            });

            // Đặt địa chỉ hiện tại làm mặc định
            address.setIsAddressDefault(true);
            addressRepository.save(address);
        } else {
            throw new RuntimeException("Address not found");
        }
    }




    @Override
    public List<CustomerResponse> filterCustomers(String searchText,
                                                  String status,
                                                  LocalDateTime startDate,
                                                  LocalDateTime endDate,
                                                  Integer minAge,
                                                  Integer maxAge) {
        List<Customer> customers = customerRepository.findAll();
        customers = customers.stream()
                .filter(customer -> customer.getFullName().toLowerCase().contains(searchText.toLowerCase()) ||
                        customer.getPhoneNumber().contains(searchText))
                .filter(customer -> status.equals("Tất cả") ||
                        (status.equals("Kích hoạt") && customer.getStatus() == 1) ||
                        (status.equals("Khóa") && customer.getStatus() == 0))
                .filter(customer -> {
                    if (startDate != null && endDate != null) {
                        return !customer.getDateBirth().isBefore(startDate) && !customer.getDateBirth().isAfter(endDate);
                    }
                    return true;
                })
                .filter(customer -> {
                    int age = LocalDateTime.now().getYear() - customer.getDateBirth().getYear();
                    return age >= minAge && age <= maxAge;
                })
                .collect(Collectors.toList());

        return customers.stream().map(CustomerResponse::new).collect(Collectors.toList());
    }

    @Override
    public boolean checkEmailExists(String email) {
        return customerRepository.findByEmail(email) != null;
    }
    @Override
    public boolean checkPhoneExists(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber) != null;
    }

    @Override
    public List<CustomerResponse> findCustomerByQuery(String query) {

        List<Customer> customers = customerRepository.searchByKeyword(query);
        List<CustomerResponse> customerResponses = customers.stream()
                .map((customer) -> new CustomerResponse(customer))
                .toList();
        return customerResponses;
    }


    @Override
    public Page<CustomerResponse> filterAndSearchCustomers(String searchText, String status,
                                                           LocalDateTime startDate, LocalDateTime endDate,
                                                           Integer minAge, Integer maxAge,
                                                           Integer size, Integer page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Customer> spec = Specification.where(null);

        // Lọc theo trạng thái (AccountStatus)
        if (status != null && !status.isEmpty()) {
            AccountStatus accountStatus = AccountStatus.valueOf(status);
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), accountStatus));
        }

        // Tìm kiếm theo tên, số điện thoại, email hoặc CCCD
        if (searchText != null && !searchText.isEmpty()) {
            String pattern = "%" + searchText + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("fullName"), pattern),
                            criteriaBuilder.like(root.get("phoneNumber"), pattern),
                            criteriaBuilder.like(root.get("email"), pattern),
                            criteriaBuilder.like(root.get("CitizenId"), pattern)
                    ));
        }



        // Lọc theo ngày sinh
        if (startDate != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("dateBirth"), startDate));
        }

        if (endDate != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("dateBirth"), endDate));
        }

        // Lọc theo tuổi (dateBirth)
        if (minAge != null || maxAge != null) {
            LocalDateTime now = LocalDateTime.now();
            if (minAge != null) {
                LocalDateTime maxBirthDate = now.minusYears(minAge);
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThanOrEqualTo(root.get("dateBirth"), maxBirthDate));
            }
            if (maxAge != null) {
                LocalDateTime minBirthDate = now.minusYears(maxAge);
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get("dateBirth"), minBirthDate));
            }
        }

        Page<Customer> customerPage = customerRepository.findAll(spec, pageable);
        List<CustomerResponse> responses = customerPage.getContent().stream()
                .map(customer -> new CustomerResponse(customer))
                .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, customerPage.getTotalElements());
    }

}