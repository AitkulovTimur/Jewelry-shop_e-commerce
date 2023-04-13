package com.service.jewelry.model;


public record ProductDto(int vendorCode, String name, String gender, double price, String description, int quantity) {

}
