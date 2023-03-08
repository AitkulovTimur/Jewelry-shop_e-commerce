package com.service.jewelry.repo;

import com.service.jewelry.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {


}
