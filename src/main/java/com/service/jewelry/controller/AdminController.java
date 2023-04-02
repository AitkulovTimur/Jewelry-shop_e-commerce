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

    @GetMapping("all_products")
    public String getAllProducts(Model model) {
        model.addAttribute("listProducts", productService.getAllProducts());
        return "administration";
    }

    @GetMapping("add_product")
    public String returnAddProductPage(Model model) {
        model.addAttribute("product", new ProductEntity());
        return "new_product";
    }

    @PostMapping("add_product")
    public String create(@Valid @ModelAttribute("product") ProductCreateRequest productToCreate,
                         BindingResult result,
                         Model model) {
        if (productService.existByVendorCode(productToCreate.getVendorCode()))
            result.rejectValue("vendorCode", null,
                    "Продукт с таким артиклем уже существует");
        if (productService.existsByName(productToCreate.getName()))
            result.rejectValue("name", null,
                    "Продукт с таким именем уже существует");

        if (result.hasErrors()) {
            model.addAttribute("product", productToCreate);
            return "new_product";
        }

        productService.createProduct(productToCreate);
        return "redirect:/administration/all_products?success";
    }
}
