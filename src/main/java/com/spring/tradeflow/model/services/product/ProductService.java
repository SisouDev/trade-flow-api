package com.spring.tradeflow.model.services.product;

import com.spring.tradeflow.exceptions.ResourceNotFoundException;
import com.spring.tradeflow.model.entities.product.Product;
import com.spring.tradeflow.model.repositories.product.ProductRepository;
import com.spring.tradeflow.utils.enums.product.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    public final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public List<Product> getAllProducts() {
        return (List<Product>) productRepository.findAll();
    }

    public Product createProducts(List<Product> products) {
        return (Product) productRepository.saveAll(products);
    }

    public int updateProductName(Long id, String newName) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setName(newName);
        return productRepository.updateName(id, newName);
    }

    public int updateDescription(Long id, String newDescription) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setDescription(newDescription);
        return productRepository.updateDescription(id, newDescription);
    }

    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Product not found");
        }
    }

    public int updateProductPrice(Long id, Double price) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setPrice(price);
        return productRepository.updateProductPrice(id, price);
    }

    public int updateStock(Long id, Integer stock) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setStock(stock);
        return productRepository.updateStockById(id, stock);
    }

    public int updateImageUrl(Long id, String imageUrl) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setImageUrl(imageUrl);
        return productRepository.updateImageUrl(id, imageUrl);
    }

    public List<Product> findLowStockProducts() {
        return productRepository.findByStockLessThan(5);
    }

    public Product applyDiscount(Long productId, Double discountPercentage) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        double originalPrice = product.getPrice();
        double discountAmount = originalPrice * (discountPercentage / 100);
        double newPrice = originalPrice - discountAmount;
        product.setPrice(newPrice);
        return productRepository.save(product);
    }

    public List<Product> findProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Product> searchProductsByKeyword(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    public List<Product> findProductByType(ProductType type) {
        return productRepository.findProductByType(type);
    }
}
