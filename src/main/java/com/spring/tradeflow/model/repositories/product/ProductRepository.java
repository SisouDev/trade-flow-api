package com.spring.tradeflow.model.repositories.product;

import com.spring.tradeflow.model.entities.product.Product;
import com.spring.tradeflow.utils.enums.product.ProductType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductByType(ProductType type);
    List<Product> findByStockLessThan(Integer stock);
    List<Product> findByNameContaining(String name);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByPriceBetween(Double lower, Double higher);
    List<Product> findByPriceGreaterThan(Double lower);
    List<Product> findByPriceLessThan(Double lower);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.price = :price WHERE p.id = :productId")
    int updateProductPrice(Long productId, Double price);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.name = :name WHERE p.id = :productId")
    int updateName(Long productId, String name);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.description = :description WHERE p.id = :productId")
    int updateDescription(Long productId, String description);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.stock = :stock WHERE p.id = :productId")
    int updateStockById(Long productId, Integer stock);

    @Modifying
    @Transactional
    void deleteById(Long productId);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.imageUrl = :imageUrl WHERE p.id = :productId")
    int updateImageUrl(Long productId, String imageUrl);
}
