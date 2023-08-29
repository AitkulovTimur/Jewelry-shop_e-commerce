package com.service.jewelry.model.wrapper;

import com.service.jewelry.model.entity.ProductEntity;
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
