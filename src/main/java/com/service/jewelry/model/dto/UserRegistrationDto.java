package com.service.jewelry.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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

    @NotEmpty(message = "Пожалуйста, введите имя")
    @Pattern(regexp = "^[A-ЯЁ][а-яё]+$", message = "Пожалуйста, введите имя в верном формате")
    private String firstName;

    @NotEmpty(message = "Пожалуйста, введите фамилию")
    @Pattern(regexp = "^[A-ЯЁ][а-яё]+$", message = "Пожалуйста, введите фамилию в верном формате")
    private String lastName;

    @NotEmpty(message = "Пожалуйста, введите емэйл")
    @Email(message = "Емэйл не проходит валидацию")
    private String email;

    @NotEmpty(message = "Пожалуйста, введите пароль")
    private String password;
}