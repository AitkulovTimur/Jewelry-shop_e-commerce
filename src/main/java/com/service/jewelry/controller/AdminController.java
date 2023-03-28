package com.service.jewelry.controller;

import com.service.jewelry.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//@RequestMapping("administration")
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
