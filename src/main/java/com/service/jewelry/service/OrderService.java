package com.service.jewelry.service;

import com.service.jewelry.model.ItemEntity;
import com.service.jewelry.model.OrderEntity;
import com.service.jewelry.model.OrderStatus;
import com.service.jewelry.model.User;
import com.service.jewelry.repo.ItemRepository;
import com.service.jewelry.repo.OrderRepository;
import com.service.jewelry.repo.ProductRepository;
import com.service.jewelry.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
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

        List<ItemEntity> items = itemsInOrder.stream()
                .map(id -> {
                    ItemEntity item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
                    productRepository.updateQuantity(item.getQuantity(), item.getProductEntity().getVendorCode());
                    return itemRepository.save(item.withOrder(newOrder));
                })
                .collect(Collectors.toList());
        newOrder.setItems(items);

        orderRepository.saveAndFlush(newOrder);
    }

    public List<OrderEntity> getAllOrdersForAdmin() {
        return orderRepository.findAll();
    }
}
