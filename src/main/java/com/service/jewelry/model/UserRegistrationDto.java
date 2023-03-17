package com.service.jewelry.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {

    @NotEmpty(message = "Please enter valid first name.")
    private String firstName;

    @NotEmpty(message = "Please enter valid last name.")
    private String lastName;

    @NotEmpty(message = "Please enter valid email.")
    private String email;

    @NotEmpty(message = "Please enter valid password.")
    private String password;
}