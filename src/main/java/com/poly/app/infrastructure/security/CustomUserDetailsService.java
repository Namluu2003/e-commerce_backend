package com.poly.app.infrastructure.security;


import com.poly.app.domain.repository.CustomerRepository;
import com.poly.app.domain.repository.StaffRepository;
import com.poly.app.domain.model.Customer;
import com.poly.app.domain.model.Staff;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Customer customer = customerRepository.findByEmail(username);
        if (customer != null) {
            return customer;
        }

        Staff staff = staffRepository.findByEmail(username);
        if (staff != null) {
            return staff;
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
