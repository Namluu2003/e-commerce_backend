package com.poly.app.infrastructure.security;

import com.poly.app.domain.model.Customer;
import com.poly.app.domain.model.Staff;
import com.poly.app.infrastructure.exception.RestApiException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Auth {
    private final HttpSession session;
    public  Customer getCustomerAuth() {
        UserDetails user = (UserDetails) session.getAttribute("user");
        if (user == null) {
            return null;
        }

        if (user instanceof Customer) {
            return (Customer) user;
        }
        return null;
    }

    public Staff getStaffAuth() {
        UserDetails user = (UserDetails) session.getAttribute("user");
        if (user == null) {
          return null;
        }
        if (user instanceof Staff) {
            return (Staff) user;
        }
        return null;
    }

    public UserDetails getUserAuth() {
        return (UserDetails) session.getAttribute("user");
    }

}
