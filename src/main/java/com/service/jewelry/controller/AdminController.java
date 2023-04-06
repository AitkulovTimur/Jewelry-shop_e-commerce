package com.service.jewelry.controller;

import com.service.jewelry.model.ProductCreateRequest;
import com.service.jewelry.model.ProductEntity;
import com.service.jewelry.model.ProductUpdateRequest;
import com.service.jewelry.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("administration")
public class AdminController {

    @Autowired
    ProductService productService;

    @GetMapping("all_products")
    public String getAllProducts(Model model,
                                 @RequestParam(required = false, name = "vendor_code_error") String vendorCodeError,
                                 @RequestParam(required = false, name = "success_update_vendor") String vendorCodeUpdate) {
        if (vendorCodeError != null && !vendorCodeError.isBlank())
            model.addAttribute("vendor_code_error", vendorCodeError);

        if (vendorCodeUpdate != null && !vendorCodeUpdate.isBlank())
            model.addAttribute("success_update_vendor", vendorCodeUpdate);

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
        return "redirect:/administration/all_products?success_create";
    }

    @GetMapping("update/{vendor_code}")
    public String updateProduct(@PathVariable(name = "vendor_code") int vendorCode, Model model) {
        ProductEntity productEntity = productService.getProductByVendor(vendorCode);

        model.addAttribute("product", productEntity);
        return "update_product";
    }

    @PostMapping("update/{vendor_code}")
    public String updateProduct(@Valid @ModelAttribute("product") ProductUpdateRequest request,
                         BindingResult result,
                         Model model, @PathVariable(name = "vendor_code") int vendorCode) {
        ProductEntity productEntityRepo;

        try {
            productEntityRepo = productService.getProductByVendor(vendorCode);
        } catch (RuntimeException e) {
            return String.format("redirect:/administration/all_products?vendor_code_error=%s", vendorCode);
        }

        if (productService.existsByName(request.getName()))
            result.rejectValue("name", null,
                    "Продукт с таким именем уже существует");

        if (result.hasErrors()) {
            model.addAttribute("product", productEntityRepo);
            return "update_product";
        }

        productService.updateProduct(request, vendorCode);
        return  String.format("redirect:/administration/all_products?success_update_vendor=%s", vendorCode);
    }
}
