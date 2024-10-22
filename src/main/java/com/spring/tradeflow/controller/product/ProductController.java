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
        log.info("Applying discount {}", discountPercentage);
        Product product = productService.getProduct(id);
        log.info("Old price: {}", product.getPrice());
        Product updatedProduct = productService.applyDiscount(id, discountPercentage);
        log.info("New price: {}", updatedProduct.getPrice());
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping(path = "/low-stock")
    public ResponseEntity<List<Product>> findLowStockProducts() {
        log.info("Finding low stock products");
        List<Product> products = productService.findLowStockProducts();
        log.info("Low stock products returned {}", products);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> findByPriceRange(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        log.info("Finding price range {}", minPrice + " - " + maxPrice);
        List<Product> products = productService.findProductsByPriceRange(minPrice, maxPrice);
        log.info("Price range returned {}", products);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        log.info("Searching for {}", keyword);
        List<Product> products = productService.searchProductsByKeyword(keyword);
        log.info("Search returned {}", products);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Product>> findByType(@PathVariable ProductType type) {
        log.info("Finding product type {}", type);
        List<Product> products = productService.findProductByType(type);
        log.info("Product type returned {}", products);
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product {}", id);
        productService.deleteProduct(id);
        log.info("Product deleted {}", id);
        return ResponseEntity.noContent().build();
    }

}
