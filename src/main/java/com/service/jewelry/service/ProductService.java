package com.service.jewelry.service;

import com.service.jewelry.model.ProductDto;
import com.service.jewelry.model.ProductEntity;

import com.service.jewelry.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public Set<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(productEntity -> new ProductDto(
                productEntity.getVendorCode(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getDescription(),
                productEntity.getPhotoPath()
        )).sorted(Comparator.comparing(ProductDto::vendorCode)).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public ProductEntity createProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    public ProductEntity getOne(int vendorCode) {
        ProductEntity product = productRepository.findById(vendorCode).get();
        return  product;
    }


}
