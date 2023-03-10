package com.service.jewelry.controller;

import com.service.jewelry.model.ProductDto;
import com.service.jewelry.model.ProductEntity;
import com.service.jewelry.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.HashSet;
import java.util.Set;

@Controller
//Don't set @RequestMapping both on a class and method layer
public class PagesController {
    @Autowired
    ProductService productService;

    @GetMapping("/catalog")
    public String returnCatalogPage(Model model, @RequestParam(required = false) String searchQuery,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "8") int size,
                                    @RequestParam(defaultValue = "name,asc") String[] sort) {
        String sortField = sort[0];
        String sortDirection = sort[1];

        Sort.Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sortField);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order));

        Page<ProductDto> productDtoPage= productService.searchProduct(searchQuery, pageable);

        if (searchQuery != null)
            model.addAttribute("searchQuery", searchQuery);

        model.addAttribute("products", productDtoPage.getContent());
        model.addAttribute("currentPage", productDtoPage.getNumber() + 1);
        model.addAttribute("totalItems", productDtoPage.getTotalElements());
        model.addAttribute("totalPages", productDtoPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");

        return "catalog";
    }

// test endpoint
    @GetMapping("/about")
    public String returnAbout() {
        return "about";
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody ProductEntity product) {
            productService.createProduct(product);
            return ResponseEntity.ok("You have added a new product");
    }

    @GetMapping("/product/{vendorCode}")
    public String getProductByVendor(@PathVariable("vendorCode") int vendorCode, Model model) {
        model.addAttribute("product",productService.getProductByVendor(vendorCode));
        return "item";
    }

}
