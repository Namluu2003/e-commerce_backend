package com.poly.app.domain.admin.staff.service;


import com.poly.app.domain.admin.address.AddressRequest;
import com.poly.app.domain.admin.customer.response.AddressResponse;
import com.poly.app.domain.admin.staff.request.StaffRequest;
import com.poly.app.domain.admin.staff.response.StaffReponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StaffService {

    StaffReponse createStaff(StaffRequest staffRequest);

    StaffReponse updateStaff(Integer id, StaffRequest staffRequest);

    void deleteStaff(Integer id);


    StaffReponse getStaffById(Integer id);

    List<StaffReponse> getAllStaff();

    StaffReponse getStaffByEmail(String email);

    AddressResponse updateAddress(Integer addressId, AddressRequest addressRequest);

    void deleteAddress(Integer addressId);

    AddressResponse addAddress(Integer staffId, AddressRequest addressRequest);

    void setDefaultAddress(Integer addressId);

    List<StaffReponse> filterStaff(String searchText, String status, String dobFrom, String dobTo, Integer ageFrom, Integer ageTo);
    boolean checkEmailExists(String email);

    boolean checkPhoneExists(String phoneNumber);

    void saveEmployeesFromExcel(MultipartFile file) throws IOException;

    void updateLastSeen();
    void logoutStatus(Integer id);
    void loginStatus(Integer id);
}