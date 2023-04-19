package com.service.jewelry.controller;

import com.service.jewelry.model.CartEntity;
import com.service.jewelry.model.ItemEntity;
import com.service.jewelry.model.ItemQuantityUpdateRequest;
import com.service.jewelry.model.ProductDto;
import com.service.jewelry.model.QuantityUpdateRequestsWrapper;
import com.service.jewelry.model.ReviewDto;
import com.service.jewelry.model.ReviewEntity;
import com.service.jewelry.service.AuthService;
import com.service.jewelry.service.CartService;
import com.service.jewelry.service.OrderService;
import com.service.jewelry.service.ProductService;
import com.service.jewelry.service.ReviewService;
import com.service.jewelry.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Controller
public class PagesController {
    @Autowired
    ProductService productService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    AuthService authService;

    @Autowired
    CartService cartService;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @GetMapping("/catalog")
    public String returnCatalogPage(Model model, @RequestParam(required = false) String searchQuery,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "8") int size,
                                    @RequestParam(defaultValue = "name,asc") String[] sort,
                                    @RequestParam(name = "success_order", required = false) boolean successOrder) {
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

        if (successOrder)
            model.addAttribute("successOrder", true);

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

    @GetMapping("/product/{vendorCode}")
    public String getOneProduct(@PathVariable("vendorCode") int vendorCode, Model model,
                                @RequestParam(value = "add_success", required = false, defaultValue = "false") boolean addSuccess,
                                @RequestParam(value = "quantity_limit", required = false, defaultValue = "false") boolean quantityLimit) {

        if (addSuccess)
            model.addAttribute("add_success", true);
        else if (quantityLimit)
            model.addAttribute("quantity_limit", true);

        model.addAttribute("product", productService.getProductByVendor(vendorCode));
        return "item";
    }

    @GetMapping("/order/{vendorCode}")
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

    @GetMapping("/cart")
    public String showCart(Model model,
                           @RequestParam(name = "sum", required = false, defaultValue = "false") boolean sumOfOrder) {
        CartEntity cart;
        try {
            cart = cartService.getCartByUId(authService.getAuthUserId());
        } catch (Exception e) {
            return "redirect:/catalog";
        }

        if (cart.getItems() == null)
            cart.setItems(new ArrayList<>());

        List<ItemQuantityUpdateRequest> listForFilling = cart.getItems().stream().map(item ->
                ItemQuantityUpdateRequest.builder().id(item.getId())
                        .maxQuantity(item.getMaxQuantity())
                        .quantity(item.getQuantity())
                        .productEntity(item.getProductEntity())
                        .build()).collect(Collectors.toList());

        model.addAttribute("show_sum", sumOfOrder);

        QuantityUpdateRequestsWrapper quantityUpdateRequestsWrapper = QuantityUpdateRequestsWrapper.builder()
                .itemsWithNewQuantity(listForFilling)
                .userCustomName("")
                .userNumber("").build();

        model.addAttribute("wrapper", quantityUpdateRequestsWrapper);
        model.addAttribute("sum_of_cart",
                cart.getItems().stream().mapToDouble(item -> {
                    double sum = item.getProductEntity().getPrice();
                    return sum * item.getQuantity();
                }).sum());

        return "cart";
    }

    @PostMapping("/add_item_to_cart/{vendorCode}")
    public String addItemToCart(@PathVariable int vendorCode) {

        try {
            cartService.addNewItemToCart(vendorCode, authService.getAuthUserId());
        } catch (RuntimeException e) {
            return "redirect:/product/" + vendorCode + "?quantity_limit=true";
        }

        return "redirect:/product/" + vendorCode + "?add_success=true";
    }

    @GetMapping("/remove_from_cart/{vendorCode}")
    public String removeFromCart(@PathVariable int vendorCode) {
        cartService.removeItemFromCart(vendorCode, authService.getAuthUserId());

        return "redirect:/cart";
    }

    // Писать логику в контроллере - это такой кринж
    @PostMapping("create_order")
    public String createOrder(@ModelAttribute QuantityUpdateRequestsWrapper wrapper,
                              Model model,
                              @RequestParam(name = "show_sum", required = true, defaultValue = "false") boolean showSum) {
        CartEntity cart = cartService.getCartByUId(authService.getAuthUserId());

        if (showSum) {
            cartService.updateQuantities(wrapper.getItemsWithNewQuantity());
            return "redirect:/cart?sum=true";
        }

        //Found shared references to a collection: com.service.jewelry.model.OrderEntity.items
        List<Integer> errorEscaping = cart.getItems().stream().map(ItemEntity::getId).collect(Collectors.toList());
        cartService.deleteCart(cart.getId());

        orderService.createNewOrder(errorEscaping, wrapper.getUserNumber(), wrapper.getUserCustomName());


        return "redirect:/catalog?success_order=true";
    }
}
