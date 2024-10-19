package com.spring.tradeflow.model.entities;

import com.spring.tradeflow.exceptions.InvalidDataException;
import com.spring.tradeflow.utils.enums.ProductType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Setter
    private String name;

    @Column(nullable = false)
    @Setter
    private String description;

    @Column(nullable = false)
    @Setter
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    public Product() {
    }

    public Product(String name, String description,  Double price, Integer stock, ProductType type) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.type = type;
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

    @Override
    public String toString() {
        return "Product{" +
                "description='" + description + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
