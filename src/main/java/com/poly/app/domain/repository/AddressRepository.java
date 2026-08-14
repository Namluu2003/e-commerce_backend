package com.poly.app.domain.repository;

import com.poly.app.domain.model.Address;
import com.poly.app.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Integer> {
    List<Address> findByCustomerId(Integer id);


    Address findFirstByCustomerIdAndIsAddressDefault(Integer id, boolean b);


    Address findFirstByCustomerIdAndProvinceIdAndDistrictIdAndWardIdAndSpecificAddress
            (Integer id,String province, String district, String ward, String spaci);


}
