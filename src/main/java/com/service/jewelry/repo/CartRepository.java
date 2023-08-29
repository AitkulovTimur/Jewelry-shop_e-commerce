package com.service.jewelry.repo;

import com.service.jewelry.model.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer> {

    CartEntity findByUserId(int userId);

    List<CartEntity> findAllByUserId(int userId);

    boolean existsByUserId(int userId);

}
