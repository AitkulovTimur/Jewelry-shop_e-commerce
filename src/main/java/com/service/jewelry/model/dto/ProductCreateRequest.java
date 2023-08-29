package com.service.jewelry.model.dto;


import com.service.jewelry.model.Gender;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

//Don't make it record @Anton Belyakov
@Value
public class ProductCreateRequest {
    @Min(1)
    int vendorCode;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 7, max = 30, message = "Неправильная длина имени (валидное имя от 7 - 10 символов)")
    String name;

    Gender gender;

    @Min(value = 500, message = "Слишком низкая цена")
    @Max(value = 6000000, message = "Слишком высокая цена")
    double price;

    @Size(max = 255, message = "Слишком большое описание")
    String description;

    @Min(value = 1, message = "Слишком маленькое количество продукта")
    @Max(value = 1000, message = "Слишком большое количество продукта")
    int quantity;
}
