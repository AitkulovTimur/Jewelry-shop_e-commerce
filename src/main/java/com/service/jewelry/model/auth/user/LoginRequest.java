package com.service.jewelry.model.auth.user;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;

    private String password;
}
