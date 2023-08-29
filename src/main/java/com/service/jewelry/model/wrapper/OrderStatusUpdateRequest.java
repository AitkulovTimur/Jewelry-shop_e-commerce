package com.service.jewelry.model.wrapper;

import com.service.jewelry.model.OrderStatus;
import com.service.jewelry.model.entity.ItemEntity;
import com.service.jewelry.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OrderStatusUpdateRequest {
    int id;

    User user;

    String userPhoneNum;

    String userCustomName;

    List<ItemEntity> items;

    LocalDateTime orderTime;

    OrderStatus status;

    double orderSum;
}
