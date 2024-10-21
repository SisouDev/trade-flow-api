package com.spring.tradeflow.controller.tests.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.tradeflow.model.entities.product.Product;
import com.spring.tradeflow.utils.enums.product.ProductType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateProduct() throws Exception {
        Product product = new Product(
                "Produto teste 2", "Teste de inserção de produto 2",
                12.90, 100, ProductType.SERIES, "image/teste2"
        );
        ObjectMapper mapper = new ObjectMapper();
        String productJson = mapper.writeValueAsString(product);

        mockMvc.perform(post("/api/product/create").contentType("application/json").content(productJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.type").value(product.getType().name()));
    }

    @Test
    public void getProductById() throws Exception {
        mockMvc.perform(get("/api/product/{id}",71))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Produto teste 2"))
                .andExpect(jsonPath("$.description").value("Teste de inserção de produto 2"))
                .andExpect(jsonPath("$.price").value(12.90))
                .andExpect(jsonPath("$.type").value(ProductType.SERIES.name()));
    }

    @Test
    public void getAllProducts() throws Exception {
        mockMvc.perform(get("/api/product/all")
        .contentType("application/json"))
                .andExpect(status().isOk());

    }

    @Test
    public void testUpdateStock() throws Exception {
        Integer newStock = 50;
        mockMvc.perform(put("/api/product/{id}/stock", 1, newStock)
                        .contentType("application/json")
                        .content(newStock.toString()))
                .andExpect(status().isNoContent());
    }

//    @Test
//    public void testApplyDiscount() throws Exception {
//        Double discountPercentage = 0.5;
//        mockMvc.perform(put("/api/product/{id}/discount", 71, discountPercentage)
//                .contentType("application/json")
//                        .content(discountPercentage.toString()))
//                .andExpect(status().isOk());
//    }

    @Test
    public void testFindLowStockProducts() throws Exception {
        mockMvc.perform(get("/api/product/low-stock")
        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindByPriceRange() throws Exception {
        double minPrice = 10.0;
        double maxPrice = 25.0;
        mockMvc.perform(get("/api/product/price-range")
                        .param("minPrice", Double.toString(minPrice))
                        .param("maxPrice", Double.toString(maxPrice))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
}
