package com.service.jewelry.controller;

import com.service.jewelry.model.ProductCreateRequest;
import com.service.jewelry.model.ProductEntity;
import com.service.jewelry.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("administration")
public class AdminController {

    @Autowired
    ProductService productService;

//    @PostMapping("/create")
//    public String create(@Valid @ModelAttribute("product") ProductCreateRequest productToCreate,
//                         BindingResult result,
//                         Model model) {
//        if (result.hasErrors()) {
//            model.addAttribute("error", result.getFieldErrors().stream().findFirst().map(DefaultMessageSourceResolvable::getDefaultMessage));
//            return "redirect:/catalog?error";
//        }
//        ProductEntity productEntity;
//        try {
//            productEntity = productService.createProduct(productToCreate);
//            model.addAttribute("product", productEntity);
//            return "item";
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//            return "redirect:/catalog?error";
//        }
//    }

    @GetMapping("all_products")
    public String getAllProducts(Model model) {
        model.addAttribute("listProducts", productService.getAllProducts());
        return "administration";
    }
}
