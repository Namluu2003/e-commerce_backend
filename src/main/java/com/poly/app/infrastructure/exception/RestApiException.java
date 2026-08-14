package com.poly.app.infrastructure.exception;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
public class RestApiException extends RuntimeException {

    private final HttpStatus status;

    public RestApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}



