package com.poly.app.domain.auth.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRequest {
    private String newPassword;
    private String password;
}
