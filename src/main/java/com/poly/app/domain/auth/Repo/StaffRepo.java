package com.poly.app.domain.auth.Repo;

import com.poly.app.domain.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepo extends JpaRepository<Staff,String> {
}
