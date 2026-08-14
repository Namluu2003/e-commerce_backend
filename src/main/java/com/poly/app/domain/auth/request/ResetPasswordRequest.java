package com.poly.app.domain.auth.request;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ResetPasswordRequest {
    String token;
    String password;
}
