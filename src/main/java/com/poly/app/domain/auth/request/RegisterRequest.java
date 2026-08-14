package com.poly.app.domain.auth.request;

import com.poly.app.infrastructure.exception.MessageValidateConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = MessageValidateConstants.nameRequired)
    private String fullName;

    @NotBlank(message = MessageValidateConstants.emailRequired)
    private String email;

    @NotBlank(message = MessageValidateConstants.passwordRequired)
    private String password;



}
