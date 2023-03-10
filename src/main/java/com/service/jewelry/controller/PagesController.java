package com.service.jewelry.controller;

import com.service.jewelry.model.ProductDto;
import com.service.jewelry.model.ProductEntity;
import com.service.jewelry.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.Set;

@Controller
public class PagesController {
    @Autowired
    ProductService productService;

    @GetMapping("/catalog")
    public String returnCatalogPage(Model model) {
        Set<ProductDto> productsSet = productService.getAllProducts();

        model.addAttribute("products", productsSet);
        return "catalog";
    }

    @GetMapping("/about")
    public String returnAbout() {
        return "about";
    }

    @GetMapping("/adress")
    public String returnAdress() {
        return "adress";
    }

    @GetMapping("/cart")
    public String returnCart() {
        return "cart";
    }

    @GetMapping("/registration")
    public String returnRegistration() {
        return "registration";
    }

    @GetMapping("/authentication")
    public String returnAuthentication() {
        return "authentication";
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody ProductEntity product) {
            productService.createProduct(product);
            return ResponseEntity.ok("You have added a new product");
    }

    @GetMapping("/product{vendorCode}")
    public String getOneProduct (@PathVariable("vendorCode") int vendorCode, Model model) {
        model.addAttribute("product",productService.getOne(vendorCode));
        return "item";
    }
}
