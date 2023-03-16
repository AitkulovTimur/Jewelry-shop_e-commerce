package com.service.jewelry.exception.auth;

import com.service.jewelry.exception.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class BadCredentialsExceptionDTO implements ExceptionDTO {

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
