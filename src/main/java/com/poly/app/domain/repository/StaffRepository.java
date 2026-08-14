package com.poly.app.domain.repository;

import com.poly.app.domain.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Staff findByEmail(String email);
    Staff findByPhoneNumber(String phoneNumber);
    Staff findStaffByCode(String code);

    Staff findStaffByCitizenId(String citizenId);

    Staff findByTokenActiveAccount(String token);

}
