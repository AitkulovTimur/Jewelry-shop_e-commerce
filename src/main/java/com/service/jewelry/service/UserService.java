package com.service.jewelry.service;

import com.service.jewelry.model.User;
import com.service.jewelry.model.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
    void save(UserRegistrationDto registrationDto);
    User findUserByEmail(String email);
}