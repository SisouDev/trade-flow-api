package com.spring.tradeflow.controller.product;

import com.spring.tradeflow.model.entities.product.Product;
import com.spring.tradeflow.model.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping(path = "/{id}/stock")
    public void updateStock(@PathVariable Long id, @RequestBody Integer stock) {
       productService.updateStock(id, stock);
    }

    @GetMapping(path = "/low-stock")
    public List<Product> findLowStockProducts() {
        return productService.findLowStockProducts();
    }

    @GetMapping(path = "/price-range")
    public List<Product> findPriceRangeProducts(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        return productService.findProductsByPriceRange(minPrice, maxPrice);
    }

    @GetMapping(path = "/search")
    public List<Product> searchProductsByKeyword(@RequestParam String search) {
        return productService.searchProductsByKeyword(search);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id){
        return productService.getProduct(id);
    }

    @GetMapping("/all")
    public List<Product> getAllProduct(){
        return productService.getAllProducts();
    }
}
