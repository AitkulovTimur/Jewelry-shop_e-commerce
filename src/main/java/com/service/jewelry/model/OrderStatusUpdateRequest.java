package com.service.jewelry.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusUpdateRequest {
    int id;

    User user;

    String userPhoneNum;

    String userCustomName;

    List<ItemEntity> items;

    Instant orderTime;

    OrderStatus status;

    double orderSum;
}
