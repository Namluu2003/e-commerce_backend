package com.poly.app.domain.auth.Repo;

import com.poly.app.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KhachHangRepository extends JpaRepository<Customer,String> {
}
