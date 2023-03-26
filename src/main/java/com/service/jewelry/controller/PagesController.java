package com.service.jewelry.controller;

import com.service.jewelry.model.ProductCreateRequest;
import com.service.jewelry.model.ProductDto;
import com.service.jewelry.model.ProductEntity;
import com.service.jewelry.model.ReviewDto;
import com.service.jewelry.model.ReviewEntity;
import com.service.jewelry.service.ProductService;
import com.service.jewelry.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class PagesController {
    @Autowired
    ProductService productService;

    @Autowired
    ReviewService reviewService;

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

        Page<ProductDto> productDtoPage = productService.searchProduct(searchQuery, pageable);

        if (searchQuery != null)
            model.addAttribute("searchQuery", searchQuery);

        model.addAttribute("products", productDtoPage.getContent());
        model.addAttribute("currentPage", productDtoPage.getNumber() + 1);
        model.addAttribute("totalItems", productDtoPage.getTotalElements());
        model.addAttribute("totalPages", productDtoPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("filterField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");

        return "catalog";
    }

    @GetMapping("/about")
    public String returnAbout() {
        return "about";
    }

    @GetMapping("/address")
    public String returnAddress() {
        return "address";
    }

    @GetMapping("/cart")
    public String returnCart() {
        return "cart";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("product") ProductCreateRequest productToCreate,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", result.getFieldErrors().stream().findFirst().map(DefaultMessageSourceResolvable::getDefaultMessage));
            return "redirect:/catalog?error";
        }
        ProductEntity productEntity;
        try {
            productEntity = productService.createProduct(productToCreate);
            model.addAttribute("product", productEntity);
            return "item";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/catalog?error";
        }
    }

    @GetMapping("/product/{vendorCode}")
    public String getOneProduct(@PathVariable("vendorCode") int vendorCode, Model model) {
        model.addAttribute("product", productService.getProductByVendor(vendorCode));
        return "item";
    }

    @GetMapping("/order{vendorCode}")
    public String getOrder(@PathVariable("vendorCode") int vendorCode, Model model) {
        model.addAttribute("product", productService.getProductByVendor(vendorCode));
        return "order";
    }

    @GetMapping("/reviews")
    public String returnReviewsPage(Model model) {
        Set<ReviewDto> reviewsSet = reviewService.getAllReviews();

        model.addAttribute("allReviews", reviewsSet);
        model.addAttribute("review", new ReviewEntity());
        return "reviews";
    }

    @PostMapping("/addReview")
    public String addReview(@ModelAttribute("review") ReviewEntity review) {
        reviewService.createReview(review);
        return "redirect:/reviews";
    }

    @GetMapping("/adminka")
    public String returnAdminka(Model model) {
        return "adminka";
    }
}
