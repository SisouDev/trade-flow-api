package com.spring.tradeflow.model.entities.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.tradeflow.model.entities.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Setter
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    @Setter
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    public OrderItem(Product product, Integer quantity, Order order) {
        this.product = product;
        this.quantity = quantity;
        this.setOrder(order);
    }

    public void setOrder(Order order) {
        this.order = order;
        if (!order.getOrderItems().contains(this)) {
            order.getOrderItems().add(this);
        }
    }

    @JsonIgnore
    public Double getTotalPrice(){
        return product.getPrice() * (quantity != null ? quantity : 0);
    }

    @Override
    public String toString() {
        return String.format(
                "OrderItem {\n" +
                        "  Order Item Id: %d,\n" +
                        "  Product Name: %s,\n" +
                        "  Product Quantity: %d\n" +
                        "}",
                id,
                product.getName(),
                quantity
        );
    }
}
