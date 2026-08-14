package com.poly.app.domain.admin.staff.service.Impl;

import com.poly.app.domain.admin.address.AddressRequest;
import com.poly.app.domain.admin.customer.response.AddressResponse;
import com.poly.app.domain.admin.staff.request.StaffRequest;
import com.poly.app.domain.admin.staff.response.StaffReponse;
import com.poly.app.domain.admin.staff.service.StaffService;
import com.poly.app.domain.auth.service.AuthenticationService;
import com.poly.app.domain.model.Address;
import com.poly.app.domain.model.Role;
import com.poly.app.domain.model.Staff;
import com.poly.app.domain.repository.AddressRepository;
import com.poly.app.domain.repository.CustomerRepository;
import com.poly.app.domain.repository.RoleRepository;
import com.poly.app.domain.repository.StaffRepository;
import com.poly.app.infrastructure.email.Email;
import com.poly.app.infrastructure.email.EmailSender;
import com.poly.app.infrastructure.exception.RestApiException;
import com.poly.app.infrastructure.util.ExcelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {
    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EmailSender emailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private CustomerRepository customerRepository;

    @Override

    public StaffReponse createStaff(StaffRequest staffRequest) {
        if (staffRepository.findByEmail(staffRequest.getEmail()) != null) {
            throw new RestApiException( "Email đã tồn tại trong hệ thống!", HttpStatus.BAD_REQUEST);
        }
        if (staffRepository.findByPhoneNumber(staffRequest.getPhoneNumber()) != null) {
            throw new RestApiException("Số điện thoại đã tồn tại!", HttpStatus.BAD_REQUEST);
        }
        Staff staffExitStaff = staffRepository.findStaffByCitizenId(staffRequest.getCitizenId());

        if (staffExitStaff != null) {
            throw new RestApiException("Căn cước đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        if (customerRepository.findByEmail(staffRequest.getEmail()) != null) {
            throw new RestApiException( "Email đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        Staff staff = new Staff();
        staff.setFullName(staffRequest.getFullName());
        staff.setEmail(staffRequest.getEmail());
        staff.setPhoneNumber(staffRequest.getPhoneNumber());
        staff.setDateBirth(staffRequest.getDateBirth());
        staff.setCitizenId(staffRequest.getCitizenId());
        staff.setGender(staffRequest.getGender());
        staff.setAvatar(staffRequest.getAvatar());
        staff.setStatus(0);
        staff.setPassword(passwordEncoder.encode(staffRequest.getPassword()));

        Role role = roleRepository.findByRoleName(staffRequest.getRoleName());
        staff.setRole(role);
        staffRepository.save(staff);

        Staff staffFromDB = staffRepository.findById(staff.getId()).orElse(null);
        assert staffFromDB != null;
        Email email = new Email();
        String[] emailSend = {staffRequest.getEmail()};
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
                "        <p><strong>Email:</strong> " + staffRequest.getEmail() + "</p>\n" +
                "        <p><strong>Mật khẩu:</strong> " + staffRequest.getPassword() + "</p>\n" +
                "        <p style=\"color: #555;\">Đăng nhập ngay để trải nghiệm!</p>\n" +
                "    </div>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n");

        emailSender.sendEmail(email);
        return new StaffReponse(staffFromDB);
    }


    @Override
    public StaffReponse updateStaff(Integer id, StaffRequest staffRequest) {
        Optional<Staff> optionalStaff = staffRepository.findById(id);
        if (optionalStaff.isPresent()) {
            Staff staff = optionalStaff.get();
            staff.setFullName(staffRequest.getFullName());
            staff.setEmail(staffRequest.getEmail());
            staff.setPhoneNumber(staffRequest.getPhoneNumber());
            staff.setDateBirth(staffRequest.getDateBirth());
            staff.setCitizenId(staffRequest.getCitizenId());
            staff.setGender(staffRequest.getGender());
            staff.setAvatar(staffRequest.getAvatar());
            staff.getAddresses().clear();
            staff.setStatus(staffRequest.getStatus());
            Staff updatedStaff = staffRepository.save(staff);


            Role role = roleRepository.findByRoleName(staffRequest.getRoleName());
            staff.setRole(role);
            staffRepository.save(staff);

            return new StaffReponse(updatedStaff);
        } else {
            throw new RuntimeException("Customer not found");
        }
    }

    @Override
    public void deleteStaff(Integer id) {
        staffRepository.deleteById(id);
    }

    @Override
    public StaffReponse getStaffById(Integer id) {
        Optional<Staff> optionalStaff = staffRepository.findById(id);
        return optionalStaff.map(StaffReponse::new).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public List<StaffReponse> getAllStaff() {
        List<Staff> staff = staffRepository.findAll();
        Collections.reverse(staff);
        return staff.stream().map(StaffReponse::new).collect(Collectors.toList());
    }

    @Override
    public StaffReponse getStaffByEmail(String email) {
        Optional<Staff> optionalStaff = Optional.ofNullable(staffRepository.findByEmail(email));
        return optionalStaff.map(StaffReponse::new).orElseThrow(() -> new RuntimeException("Customer not found"));
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
    public AddressResponse addAddress(Integer staffId, AddressRequest addressRequest) {
        Optional<Staff> optionalStaff = staffRepository.findById(staffId);
        if (optionalStaff.isPresent()) {
            Staff staff = optionalStaff.get();
            Address address = Address.builder()
                    .provinceId(addressRequest.getProvinceId())
                    .districtId(addressRequest.getDistrictId())
                    .wardId(addressRequest.getWardId())
                    .specificAddress(addressRequest.getSpecificAddress())
                    .isAddressDefault(addressRequest.getIsAddressDefault())
                    .build();
            address.setStaff(staff);
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
            List<Address> addresses = addressRepository.findByCustomerId(address.getCustomer().getId());
            addresses.forEach(addr -> {
                if (addr.getIsAddressDefault()) {
                    addr.setIsAddressDefault(false);
                    addressRepository.save(addr);
                }
            });
            address.setIsAddressDefault(true);
            addressRepository.save(address);
        } else {
            throw new RuntimeException("Address not found");
        }
    }

    @Override
    public List<StaffReponse> filterStaff(String searchText, String status, String dobFrom, String dobTo, Integer ageFrom, Integer ageTo) {
        List<Staff> staffList = staffRepository.findAll();
        staffList = staffList.stream()
                .filter(staff -> searchText == null || searchText.isEmpty() ||
                        staff.getFullName().toLowerCase().contains(searchText.toLowerCase()) ||
                        staff.getPhoneNumber().contains(searchText))
                .filter(staff -> status == null || status.equals("Tất cả") ||
                        (status.equals("HOAT_DONG") && staff.getStatus() == 0) ||
                        (status.equals("CHUA_KICH_HOAT") && staff.getStatus() == 2) ||
                        (status.equals("NGUNG_HOAT_DONG") && staff.getStatus() == 1))
                .filter(staff -> {
                    if (dobFrom != null && dobTo != null) {
                        LocalDateTime dobFromDateTime = LocalDateTime.parse(dobFrom);
                        LocalDateTime dobToDateTime = LocalDateTime.parse(dobTo);
                        return !staff.getDateBirth().isBefore(dobFromDateTime) && !staff.getDateBirth().isAfter(dobToDateTime);
                    }
                    return true;
                })
                .filter(staff -> {
                    if (ageFrom != null && ageTo != null) {
                        int age = LocalDateTime.now().getYear() - staff.getDateBirth().getYear();
                        return age >= ageFrom && age <= ageTo;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        return staffList.stream().map(StaffReponse::new).collect(Collectors.toList());
    }
    @Override
    public boolean checkEmailExists(String email) {
        return staffRepository.findByEmail(email) != null;
    }
    @Override
    public boolean checkPhoneExists(String phoneNumber) {
        return staffRepository.findByPhoneNumber(phoneNumber) != null;
    }

    @Override
    public void saveEmployeesFromExcel(MultipartFile file) throws IOException {
        try {
            List<Staff> employees = ExcelHelper.excelToEmployees(file.getInputStream());
            staffRepository.saveAll(employees);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi đọc file Excel: " + e.getMessage());
        }
    }

    @Override
    public void updateLastSeen() {
            LocalDateTime now = LocalDateTime.now();
            Staff staffAuth = authenticationService.getStaffAuth();
            if(staffAuth != null) {
                staffAuth.setLastSeen(now);
                staffRepository.save(staffAuth);
            }
    }

    @Override
    public void logoutStatus(Integer id) {
        Staff staff = staffRepository.findById(id).get();
        staff.setLastSeen(LocalDateTime.now());
        staff.setIsOnline(false);
        staffRepository.save(staff);
    }

    @Override
    public void loginStatus(Integer id) {
        Staff staff = staffRepository.findById(id).get();
        staff.setIsOnline(true);
        staffRepository.save(staff);
    }


}