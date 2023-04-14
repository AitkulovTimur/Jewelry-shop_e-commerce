package com.service.jewelry.repo;

import com.service.jewelry.model.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {
}
