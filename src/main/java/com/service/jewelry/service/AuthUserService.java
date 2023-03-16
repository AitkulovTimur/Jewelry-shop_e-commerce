package com.service.jewelry.service;

import com.service.jewelry.exception.auth.BadCredentialsException;
import com.service.jewelry.exception.item.ItemAlreadyExistsException;
import com.service.jewelry.model.auth.user.AuthUserEntity;
import com.service.jewelry.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {

    @Autowired
    private AuthUserRepository authUserRepository;

    public AuthUserEntity createAuthUser(String email, String password) {
        if (email.trim().equals("") || password.trim().equals("")) {
            throw new BadCredentialsException();
        }

        if (authUserRepository.existsByEmail(email)) {
            throw new ItemAlreadyExistsException();
        }

        AuthUserEntity user = AuthUserEntity.builder()
                .email(email)
                .password(password)
                .build();

        return authUserRepository.save(user);
    }
}
