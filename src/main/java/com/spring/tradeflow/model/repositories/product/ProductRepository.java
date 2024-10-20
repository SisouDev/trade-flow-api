package com.spring.tradeflow.model.repositories.product;

import com.spring.tradeflow.model.entities.product.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
