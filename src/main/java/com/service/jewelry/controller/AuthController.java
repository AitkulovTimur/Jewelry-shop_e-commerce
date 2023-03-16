package com.service.jewelry.controller;

import com.service.jewelry.model.auth.user.AuthUserEntity;
import com.service.jewelry.model.auth.user.LoginRequest;
import com.service.jewelry.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth/")
public class AuthController {

    @Autowired
    private AuthUserService authUserService;

    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.OK)
    public AuthUserEntity signUp(@RequestBody LoginRequest request) {
        return authUserService.createAuthUser(request.getEmail(), request.getPassword());
    }
}
