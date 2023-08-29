package com.service.jewelry.controller;

import com.service.jewelry.model.entity.OrderEntity;
import com.service.jewelry.model.wrapper.OrderUpdateWrapper;
import com.service.jewelry.model.dto.ProductCreateRequest;
import com.service.jewelry.model.entity.ProductEntity;
import com.service.jewelry.model.dto.ProductUpdateRequest;
import com.service.jewelry.service.OrderService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("administration")
public class AdminController {

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    public static final String SEPARATOR = System.getProperty("file.separator");
    public static final String COMMON_REDIRECT_URL = "redirect:/administration/";
    public static final String PRODUCT_STRING = "product";

    public static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + SEPARATOR + "src" +
            SEPARATOR + "main" + SEPARATOR + "resources" + SEPARATOR + "static" + SEPARATOR + "img";

    @GetMapping("all_products")
    public String getAllProductsPage(Model model,
                                 @RequestParam(required = false, name = "vendor_code_error") String vendorCodeError,
                                 @RequestParam(required = false, name = "success_update_vendor") String vendorCodeUpdate,
                                 @RequestParam(required = false, name = "success_delete_vendor") String vendorCodeDelete
    ) {
        if (vendorCodeError != null && !vendorCodeError.isBlank())
            model.addAttribute("vendor_code_error", vendorCodeError);

        if (vendorCodeUpdate != null && !vendorCodeUpdate.isBlank())
            model.addAttribute("success_update_vendor", vendorCodeUpdate);

        if (vendorCodeDelete != null && !vendorCodeDelete.isBlank())
            model.addAttribute("success_delete_vendor", vendorCodeDelete);

        model.addAttribute("listProducts", productService.getAllProducts());
        return "administration";
    }

    @GetMapping("add_product")
    public String returnAddProductPage(Model model) {
        model.addAttribute(PRODUCT_STRING, new ProductEntity().withVendorCode(productService.getLastVendorCode() + 1));
        return "new_product";
    }

    @PostMapping("add_product")
    public String addProductToCatalog(@Valid @ModelAttribute(PRODUCT_STRING) ProductCreateRequest productToCreate,
                         BindingResult result,
                         Model model) {
        if (productService.existByVendorCode(productToCreate.getVendorCode()))
            result.rejectValue("vendorCode", null,
                    "Продукт с таким артиклем уже существует");
        if (productService.existsByName(productToCreate.getName()))
            result.rejectValue("name", null,
                    "Продукт с таким именем уже существует");

        if (result.hasErrors()) {
            model.addAttribute(PRODUCT_STRING, productToCreate);
            return "new_product";
        }

        productService.createProduct(productToCreate);
        return COMMON_REDIRECT_URL + "all_products?success_create";
    }

    @GetMapping("update/{vendor_code}")
    public String updateProduct(@PathVariable(name = "vendor_code") int vendorCode, @RequestParam(name = "success_photo", required = false) boolean successPhoto, Model model) {
        ProductEntity productEntity = productService.getProductByVendor(vendorCode);

        if (successPhoto)
            model.addAttribute("msg", "Ваше фото было загружено, но появится позже");

        model.addAttribute(PRODUCT_STRING, productEntity);
        return "update_product";
    }

    @PostMapping("update/{vendor_code}")
    public String updateProduct(@Valid @ModelAttribute(PRODUCT_STRING) ProductUpdateRequest request,
                                BindingResult result, Model model,
                                @PathVariable(name = "vendor_code") int vendorCode) {
        try {
            productService.getProductByVendor(vendorCode);
        } catch (RuntimeException e) {
            return String.format(COMMON_REDIRECT_URL + "all_products?vendor_code_error=%s", vendorCode);
        }

        if (productService.existsByName(request.getName(), vendorCode))
            result.rejectValue("name", null,
                    "Продукт с таким именем уже существует");

        if (result.hasErrors()) {
            model.addAttribute(PRODUCT_STRING, request);
            return "update_product";
        }

        productService.updateProduct(request, vendorCode);
        return String.format(COMMON_REDIRECT_URL + "all_products?success_update_vendor=%s", vendorCode);
    }

    @PostMapping("delete/{vendor_code}")
    public String deleteProduct(@PathVariable(name = "vendor_code") int vendorCode) {
        try {
            productService.deleteProduct(vendorCode);
            return String.format(COMMON_REDIRECT_URL + "all_products?success_delete_vendor=%s", vendorCode);
        } catch (RuntimeException e) {
            return String.format(COMMON_REDIRECT_URL + "all_products?vendor_code_error=%s", vendorCode);
        }
    }

    @PostMapping("upload/{vendor_code}")
    public String uploadImage(Model model, @RequestParam("image") MultipartFile file, @PathVariable("vendor_code") int vendorCode) throws IOException {
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, vendorCode + ".jpg");
        Files.write(fileNameAndPath, file.getBytes());
        return COMMON_REDIRECT_URL + "update/" + vendorCode + "?success_photo=true";
    }

    @GetMapping("orders")
    public String showAllOrderForAdmin(Model model) {
         OrderUpdateWrapper orderUpdateRequest;
        try {
            List<OrderEntity> orderEntities = orderService.getAllOrdersForAdmin();
            orderUpdateRequest = OrderService.returnAllOrdersWrapped(orderEntities);
        } catch (Exception e) {
            return COMMON_REDIRECT_URL + "all_products";
        }

        model.addAttribute("wrapper", orderUpdateRequest);

        return "orders";
    }
}
