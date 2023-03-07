package com.service.jewelry.model;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="products")
public class ProductEntity {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendor_code", updatable = false, nullable = false)
    private int vendorCode;


    @Column(name="name", nullable = false, unique = true)
    private String name;

    @Column(name="price")
    private double price;

    @Column(name="description")
    private String description;

    @Column(name="photo_path")
    private String photoPath;

}
