package com.service.jewelry.service;

import com.service.jewelry.model.dto.ProductCreateRequest;
import com.service.jewelry.model.dto.ProductDto;
import com.service.jewelry.model.entity.ProductEntity;
import com.service.jewelry.model.dto.ProductUpdateRequest;
import com.service.jewelry.repo.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

@Service
@Slf4j
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    private static final String REGEX_WHITESPACES = "\\s+";
    private static final Pattern SEARCH_STR_PATTERN = Pattern.compile("[^\\p{L}+0-9\\s.]", Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS);

    private String handleSearchString(String searchStr) {
        if (searchStr == null || searchStr.isBlank())
            return null;
        searchStr = searchStr.trim();

        Matcher matcherForbiddenSymbols = SEARCH_STR_PATTERN.matcher(searchStr);

        if (matcherForbiddenSymbols.find()) {
            log.error("Wrong symbol");
            throw new RuntimeException("Wrong symbol");
        }

        return Stream.of(searchStr.split(REGEX_WHITESPACES))
                .map(word -> format("+%s*", word.trim()))
                .collect(Collectors.joining(" "));
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(this::mapper)
                .sorted(Comparator.comparing(ProductDto::vendorCode)).toList();
    }

    public void createProduct(ProductCreateRequest request) {
        productRepository.save(this.mapper(request));
    }

    public ProductEntity updateProduct(ProductUpdateRequest request, int vendorCode) {
        return productRepository.save(ProductEntity.builder()
                .vendorCode(vendorCode)
                .name(request.getName())
                .description(request.getDescription())
                .gender(request.getGender())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build());
    }

    public ProductEntity getProductByVendor(int vendorCode) {
        return productRepository.findById(vendorCode)
                .orElseThrow(() ->
                        new RuntimeException("ProductNotFound"));
    }

    public boolean existsByName(String name, int vendorCode) {
        ProductEntity productEntity = productRepository.findById(vendorCode).orElseThrow(() -> new RuntimeException("empty"));
        return !productEntity.getName().equals(name);
    }

    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    public boolean existByVendorCode(int vendorCode) {
        return productRepository.existsByVendorCode(vendorCode);
    }

    public Page<ProductDto> searchProduct(String searchStr, Pageable pageable) {
        Page<ProductEntity> productEntities;
        try {
            searchStr = handleSearchString(searchStr);
            productEntities = searchStr != null
                    ? productRepository.getProductsBySearchString(searchStr, pageable)
                    : productRepository.findAll(pageable);
            return productEntities.map(this::mapper);
        } catch (RuntimeException e) {
            return Page.empty();
        }
    }

    public int getLastVendorCode() {
        return productRepository.findMaxVendorCode();
    }

    public void deleteProduct(int vendorCode) {
        if (!productRepository.existsByVendorCode(vendorCode))
            throw new RuntimeException("Can't delete product");

        productRepository.deleteById(vendorCode);
    }

    private ProductDto mapper(ProductEntity productEntity) {
        return new ProductDto(
                productEntity.getVendorCode(),
                productEntity.getName(),
                productEntity.getGender().toString(),
                productEntity.getPrice(),
                productEntity.getDescription(),
                productEntity.getQuantity()
        );
    }

    private ProductEntity mapper(ProductCreateRequest productCreateRequest) {
        if (productCreateRequest.getVendorCode() != 0)
            return ProductEntity.builder()
                    .vendorCode(productCreateRequest.getVendorCode())
                    .name(productCreateRequest.getName())
                    .gender(productCreateRequest.getGender())
                    .price(productCreateRequest.getPrice())
                    .description(productCreateRequest.getDescription())
                    .quantity(productCreateRequest.getQuantity())
                    .build();
        else
            return ProductEntity.builder()
                    .name(productCreateRequest.getName())
                    .gender(productCreateRequest.getGender())
                    .price(productCreateRequest.getPrice())
                    .description(productCreateRequest.getDescription())
                    .build();
    }
}
