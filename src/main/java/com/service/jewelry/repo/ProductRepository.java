package com.service.jewelry.repo;

import com.service.jewelry.model.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    @Query(value = """
            SELECT * FROM products p \s
            WHERE MATCH(name) \s
            AGAINST (:searchString in boolean mode) \s
            """,
            countQuery = """
            SELECT COUNT (*) FROM products p \s
            WHERE MATCH(name) \s
            AGAINST (:searchString in boolean mode) \s
            """,
            nativeQuery = true)
    Page<ProductEntity> getProductsBySearchString(String searchString, Pageable pageable);
}
