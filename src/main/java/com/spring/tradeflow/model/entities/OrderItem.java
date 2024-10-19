package com.spring.tradeflow.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
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
    private Order order;

    public OrderItem() {
    }

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

    public Double getTotalPrice(){
        return product.getPrice() * (quantity != null ? quantity : 0);
    }
}
