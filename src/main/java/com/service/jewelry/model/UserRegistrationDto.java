package com.service.jewelry.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationDto {

    @NotEmpty(message = "Please enter valid first name.")
    private String firstName;

    @NotEmpty(message = "Please enter valid last name.")
    private String lastName;

    @NotEmpty(message = "Please enter not empty email.")
    private String email;

    @NotEmpty(message = "Please enter valid password.")
    private String password;
}