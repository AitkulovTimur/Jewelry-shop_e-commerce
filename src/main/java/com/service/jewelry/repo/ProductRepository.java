package com.service.jewelry.repo;

import com.service.jewelry.model.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    boolean existsByVendorCode(int vendorCode);

    boolean existsByName(String name);


    @Query(value = """
            SELECT MAX(vendor_code) from products;
            """, nativeQuery = true)
    int findMaxVendorCode();

    @Modifying
    @Query(value = """
            UPDATE products
            SET quantity = quantity - :quantity
            WHERE vendor_code = :vendorCode
            """
            , nativeQuery = true)
    void updateQuantity(int quantity, int vendorCode);
}
