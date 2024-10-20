package com.spring.tradeflow.model.entities.product;

import com.spring.tradeflow.exceptions.InvalidDataException;
import com.spring.tradeflow.utils.enums.product.ProductType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;

@Entity
@Table
@Getter
@ToString
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Setter
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Column(nullable = false)
    @Setter
    @NotBlank(message = "Description cannot be empty")
    private String description;

    @Column(nullable = false)
    @Setter
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Column(nullable = false)
    @Min(value = 0, message = "Price must be greater than zero")
    private Double price;

    @Column(nullable = false)
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @Column(nullable = false)
    @Setter
    private String imageUrl;


    public Product(String name, String description,  Double price, Integer stock, ProductType type, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.type = type;
        this.imageUrl = imageUrl;
    }

    public void setStock(Integer stock) {
        if (stock < 0){
            throw new InvalidDataException("Stock cannot be negative");
        }
        this.stock = stock;
    }

    public void setPrice(Double price) {
        if (price == null || price <= 0.0){
            throw new InvalidDataException("Price cannot be negative");
        }
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
