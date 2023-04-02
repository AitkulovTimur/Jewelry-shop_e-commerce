package com.service.jewelry.service;

import com.service.jewelry.model.ProductCreateRequest;
import com.service.jewelry.model.ProductDto;
import com.service.jewelry.model.ProductEntity;
import com.service.jewelry.repo.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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

    public ProductEntity createProduct(ProductCreateRequest request) {
        return productRepository.save(this.mapper(request));
    }

    public ProductEntity getProductByVendor(int vendorCode) {
        return productRepository.findById(vendorCode).orElseThrow(() -> new RuntimeException("ProductNotFound"));
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

    private ProductDto mapper(ProductEntity productEntity) {
        return new ProductDto(
                productEntity.getVendorCode(),
                productEntity.getName(),
                productEntity.getGender().toString(),
                productEntity.getPrice(),
                productEntity.getDescription()
        );
    }


    private ProductEntity mapper(ProductCreateRequest productCreateRequest) {
        return ProductEntity.builder()
                .name(productCreateRequest.getName())
                .gender(productCreateRequest.getGender())
                .price(productCreateRequest.getPrice())
                .description(productCreateRequest.getDescription())
                .build();
    }
}
