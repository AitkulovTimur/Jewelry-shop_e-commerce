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
//Don't set @RequestMapping both on a class and method layer. Xz yabanutaya huinya 4 chasa potratil ne rabotalo
public class PagesController {
    @Autowired
    ProductService productService;

    @GetMapping("/catalog")
    public String returnCatalogPage(Model model) {
        Set<ProductDto> productsSet = productService.getAllProducts();

        model.addAttribute("products", productsSet);
        return "catalog";
    }

// test endpoint
    @GetMapping("/about")
    public String returnAbout() {
        return "about";
    }
    /*TODO: @Belyakov add new endpoint and method in product service
    to add new product + to get new product(only this with ui)
     */

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
