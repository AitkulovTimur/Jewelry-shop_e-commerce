package com.service.jewelry.service;

import com.service.jewelry.model.Role;
import com.service.jewelry.model.User;
import com.service.jewelry.model.UserRegistrationDto;
import com.service.jewelry.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void save(UserRegistrationDto registrationDto) {
        String p = passwordEncoder.encode(registrationDto.getPassword());
        User user = new User(registrationDto.getFirstName(),
                registrationDto.getLastName(), registrationDto.getEmail(),
                p, List.of(new Role("ROLE_USER")));

        userRepository.save(user);
    }

    @Override
    public void saveAdmin(UserRegistrationDto registrationDto) {
        if (!userRepository.existsByEmail(registrationDto.getEmail())) {
            String p = passwordEncoder.encode(registrationDto.getPassword());
            User user = new User(registrationDto.getFirstName(),
                    registrationDto.getLastName(), registrationDto.getEmail(),
                    p, List.of(new Role("ROLE_ADMIN")));

            userRepository.save(user);
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}