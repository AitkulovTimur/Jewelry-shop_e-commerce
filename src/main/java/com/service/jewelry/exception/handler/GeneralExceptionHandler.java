package com.service.jewelry.exception.handler;

import com.service.jewelry.exception.ExceptionDTO;
import com.service.jewelry.exception.auth.BadCredentialsException;
import com.service.jewelry.exception.auth.BadCredentialsExceptionDTO;
import com.service.jewelry.exception.item.ItemAlreadyExistsException;
import com.service.jewelry.exception.item.ItemAlreadyExistsExceptionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@ControllerAdvice
public class GeneralExceptionHandler extends DefaultHandlerExceptionResolver {

    @ExceptionHandler({ItemAlreadyExistsException.class})
    public ResponseEntity<Object> handleItemAlreadyExists() {
        return buildResponse(new ItemAlreadyExistsExceptionDTO());
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleBadCredentials() {
        return buildResponse(new BadCredentialsExceptionDTO());
    }

    private ResponseEntity<Object> buildResponse(ExceptionDTO exceptionDTO) {
        return new ResponseEntity<>(exceptionDTO.getStatusCode());
    }
}
