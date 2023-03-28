package com.service.jewelry.model;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class ProductCreateRequest {

    @NotBlank(message = "Email cannot be empty")
    @Size(min = 7, max = 50, message = "Wrong email length")
    String name;
    @NotBlank(message = "Email cannot be empty")
    Gender gender;

    @NotBlank(message = "Email cannot be empty")
    @Size(min = 1, max = 10, message = "Wrong price length")
    @Min(value = 500, message = "Price lower bound violation")
    @Max(value = 6000000, message = "Price higher bound violation")
    double price;

    String description;
    String photoPath;
}
