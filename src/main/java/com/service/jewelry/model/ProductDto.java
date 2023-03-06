package com.service.jewelry.model;


public record ProductDto(String id, String name, double price, String description, String photoPath, int vendorCode) {
}
