package com.service.jewelry.model.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateWrapper {
    List<OrderStatusUpdateRequest> ordersWithNewStatuses;
}
