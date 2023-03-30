package com.service.jewelry.repo;

import com.service.jewelry.model.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {


}
