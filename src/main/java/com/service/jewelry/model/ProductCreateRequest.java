package com.service.jewelry.model;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

//Don't make it a record @Anton Belyakov
@Value
public class ProductCreateRequest {
    @Min(1)
    int vendorCode;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 7, max = 50, message = "Неправильная длина имени, возможно она слишком большая или маленькая")
    String name;

    Gender gender;

    @Min(value = 500, message = "Слишком низкая цена")
    @Max(value = 6000000, message = "Слишком высокая цена")
    double price;

    @Size(max = 255, message = "Слишком большое описание")
    String description;
}
