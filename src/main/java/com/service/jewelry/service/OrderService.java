package com.service.jewelry.service;

import com.service.jewelry.model.OrderStatus;
import com.service.jewelry.model.entity.ItemEntity;
import com.service.jewelry.model.entity.OrderEntity;
import com.service.jewelry.model.entity.User;
import com.service.jewelry.model.wrapper.OrderStatusUpdateRequest;
import com.service.jewelry.model.wrapper.OrderUpdateWrapper;
import com.service.jewelry.repo.ItemRepository;
import com.service.jewelry.repo.OrderRepository;
import com.service.jewelry.repo.ProductRepository;
import com.service.jewelry.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ItemRepository itemRepository;

    @Transactional
    public void createNewOrder(List<Integer> itemsInOrder, String userNumber, String userCustomName) {
        User user = userRepository.findById(authService.getAuthUserId()).orElseThrow(() -> new RuntimeException("user not found"));

        OrderEntity newOrder = OrderEntity.builder()
                .orderTime(Instant.now())
                .user(user)
                .userPhoneNum(userNumber)
                .userCustomName(userCustomName)
                .status(OrderStatus.WAITING_APPROVE)
                .build();
        orderRepository.save(newOrder);

        ArrayList<ItemEntity> items = itemsInOrder.stream()
                .map(id -> {
                    ItemEntity item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
                    productRepository.updateQuantity(item.getQuantity(), item.getProductEntity().getVendorCode());
                    return itemRepository.save(item.withOrder(newOrder));
                })
                .collect(Collectors.toCollection(ArrayList::new));
        newOrder.setItems(items);

        orderRepository.saveAndFlush(newOrder);
    }

    public List<OrderEntity> getAllOrdersForAdmin() {
        return new ArrayList<>(orderRepository.findAll());
    }

    //return wrapper object for list of all orders in the admin panel
    //we need this wrapper for proper Thymeleaf mapping in orders.html template
    public static OrderUpdateWrapper returnAllOrdersWrapped(List<OrderEntity> orderEntities) {
        List<OrderStatusUpdateRequest> listForFilling = orderEntities.stream().map(order -> {
                    List<ItemEntity> items = order.getItems();
                    return OrderStatusUpdateRequest.builder().id(order.getId())
                            .user(order.getUser())
                            .userPhoneNum(order.getUserPhoneNum())
                            .userCustomName(order.getUserCustomName())
                            .items(items == null || items.isEmpty() ? new ArrayList<>() : items)
                            .orderTime(LocalDateTime.ofInstant(order.getOrderTime(), ZoneId.systemDefault()))
                            .status(order.getStatus())
                            .orderSum(order.getItems().stream().mapToDouble(item -> {
                                double sum = item.getProductEntity().getPrice();
                                return sum * item.getQuantity();
                            }).sum()).build();
                }
        ).collect(Collectors.toList());

        return OrderUpdateWrapper.builder().ordersWithNewStatuses(listForFilling).build();
    }

    public static boolean validatePhoneNum(String phoneNumber) {
        Pattern patternNum = Pattern.compile("^(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,14}(\\s*)?$");
        Matcher matcherNum = patternNum.matcher(phoneNumber);

        return matcherNum.find();
    }

    public static boolean validateName(String customName) {
        Pattern patternName = Pattern.compile("^[A-ЯЁ][а-яё]+$");
        Matcher matcherName = patternName.matcher(customName);

        return matcherName.find();
    }
}
