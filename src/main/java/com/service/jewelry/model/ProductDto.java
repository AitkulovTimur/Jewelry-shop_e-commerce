package com.service.jewelry.model;


public record ProductDto(int vendorCode, String name, Gender gender, double price, String description) {

}
