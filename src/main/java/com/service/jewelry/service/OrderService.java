package com.service.jewelry.service;

import com.service.jewelry.model.ItemEntity;
import com.service.jewelry.model.OrderEntity;
import com.service.jewelry.model.OrderStatus;
import com.service.jewelry.model.User;
import com.service.jewelry.repo.OrderRepository;
import com.service.jewelry.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    public void createNewOrder(List<ItemEntity> itemsInOrder, String userNumber, String userCustomName) {
        User user = userRepository.findById(authService.getAuthUserId()).orElseThrow(() -> new RuntimeException("user not found"));

        orderRepository.saveAndFlush(OrderEntity.builder()
                .orderTime(Instant.now())
                .user(user)
                .items(itemsInOrder)
                .userPhoneNum(userNumber)
                .userCustomName(userCustomName)
                .status(OrderStatus.WAITING_APPROVE)
                .build());
    }
}
