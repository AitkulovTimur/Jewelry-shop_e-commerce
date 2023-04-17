package com.service.jewelry.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemQuantityUpdateRequest {

    int id;

    int quantity;

    int maxQuantity;

    ProductEntity productEntity;
}
