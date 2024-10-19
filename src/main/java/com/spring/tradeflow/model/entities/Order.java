package com.spring.tradeflow.model.entities;

import com.spring.tradeflow.utils.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @Setter
    private Client client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false)
    @Setter
    private Double shippingPrice;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate orderDate;

    public Order() {
    }

    public Order(Client client, OrderStatus status, Double shippingPrice) {
        this.client = client;
        this.status = status;
        this.shippingPrice = shippingPrice;
        this.orderItems = new ArrayList<>();
    }

    public Double getTotalOrderPrice() {
        return orderItems.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum() + shippingPrice;
    }

}
