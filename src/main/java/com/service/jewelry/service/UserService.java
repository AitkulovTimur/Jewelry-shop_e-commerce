package com.service.jewelry.service;

import com.service.jewelry.model.entity.User;
import com.service.jewelry.model.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
    void save(UserRegistrationDto registrationDto);

    void saveAdmin(UserRegistrationDto registrationDto);
    User findUserByEmail(String email);
}