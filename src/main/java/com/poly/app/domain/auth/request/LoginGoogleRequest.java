package com.poly.app.domain.auth.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginGoogleRequest {
    private String email;
    private String name;
    private String image;
}
