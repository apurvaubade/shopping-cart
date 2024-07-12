package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p " +
            "JOIN p.category c " +
            "JOIN c.section s " +
            "WHERE (:categoryName IS NULL OR c.name = :categoryName) " +
            "AND (:sectionName IS NULL OR s.name = :sectionName) " +
            "AND (:name IS NULL OR p.name LIKE %:name%) " +
            "AND (:price IS NULL OR p.price = :price) " +
            "AND (:discount IS NULL OR p.discount = :discount)")
    List<Product> findByCategoryAndSectionAndProductDetails(@Param("categoryName") String categoryName,
                                                            @Param("sectionName") String sectionName,
                                                            @Param("name") String name,
                                                            @Param("price") BigDecimal price,
                                                            @Param("discount") BigDecimal discount);
}
