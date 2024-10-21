package com.spring.tradeflow.controller.product;

import com.spring.tradeflow.model.entities.product.Product;
import com.spring.tradeflow.model.services.product.ProductService;
import com.spring.tradeflow.utils.enums.product.ProductType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        log.info("Creating product {}", product);
        Product createdProduct = productService.createProduct(product);
        log.info("Product created {}", createdProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        log.info("Getting product {}", id);
        Product product = productService.getProduct(id);
        log.info("Product returned {}", product);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("Getting all products");
        List<Product> products = productService.getAllProducts();
        log.info("All Products returned {}", products);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Void> updateStock(@PathVariable Long id, @RequestBody Integer stock) {
        log.info("Updating stock {}", stock);
        productService.updateStockById(id, stock);
        log.info("Stock updated {}", stock);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/discount")
    public ResponseEntity<Product> applyDiscount(@PathVariable Long id, @RequestParam Double discountPercentage) {
        Product updatedProduct = productService.applyDiscount(id, discountPercentage);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping(path = "/low-stock")
    public ResponseEntity<List<Product>> findLowStockProducts() {
        List<Product> products = productService.findLowStockProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> findByPriceRange(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        List<Product> products = productService.findProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.searchProductsByKeyword(keyword);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Product>> findByType(@PathVariable ProductType type) {
        List<Product> products = productService.findProductByType(type);
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
