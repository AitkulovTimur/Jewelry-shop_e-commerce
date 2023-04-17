package com.service.jewelry.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuantityUpdateRequestsWrapper {
    List<ItemQuantityUpdateRequest> itemsWithNewQuantity;

    String userNumber;

    String userCustomName;
}
