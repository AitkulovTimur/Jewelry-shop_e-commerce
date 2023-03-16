package com.service.jewelry.exception.item;

import com.service.jewelry.exception.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ItemAlreadyExistsExceptionDTO implements ExceptionDTO {

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.CONFLICT;
    }
}
