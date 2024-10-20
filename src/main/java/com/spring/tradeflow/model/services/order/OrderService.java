package com.spring.tradeflow.model.services.order;

import com.spring.tradeflow.exceptions.InvalidDataException;
import com.spring.tradeflow.exceptions.ResourceNotFoundException;
import com.spring.tradeflow.model.entities.client.Client;
import com.spring.tradeflow.model.entities.order.Order;
import com.spring.tradeflow.model.entities.order.OrderItem;
import com.spring.tradeflow.model.entities.product.Product;
import com.spring.tradeflow.model.repositories.client.ClientRepository;
import com.spring.tradeflow.model.repositories.order.OrderRepository;
import com.spring.tradeflow.model.repositories.product.ProductRepository;
import com.spring.tradeflow.utils.enums.order.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    public final OrderRepository orderRepository;
    public final ProductRepository productRepository;
    public final ClientRepository clientRepository;

    @Autowired
    public OrderService(final OrderRepository orderRepository, final ProductRepository productRepository, final ClientRepository clientRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
    }

    public Order createOrder(Order order) {
        Client orderClient = clientRepository.findById(order.getClient().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Client not found")
        );
        order.setClient(orderClient);

        for (OrderItem item : order.getOrderItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            if (product.getPrice() == null || product.getPrice() <= 0) {
                throw new InvalidDataException("Product price is invalid for product ID: " + product.getId());
            }
            item.setOrder(order);
            item.setProduct(product);
        }
        return orderRepository.save(order);
    }

    public Order addProducts(Long orderId, List<OrderItem> orderItems) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        List<Long> productIds = orderItems.stream().map(orderItem -> orderItem.getProduct().getId()).toList();
        List<Product> products = (List<Product>) productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new ResourceNotFoundException("Some products not found");
        }
        Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, product -> product));

        for (OrderItem orderItem : orderItems) {
            Product product = productMap.get(orderItem.getProduct().getId());
            if (orderItem.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }
            orderItem.setProduct(product);
            orderItem.setOrder(order);
        }

        order.setOrderItems(orderItems);
        return orderRepository.save(order);
    }

    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public List<Order> getOrders(Long clientId) {
        List<Order> clientOrders = new ArrayList<>();
        List<Order> orders = (List<Order>) orderRepository.findAll();
        for (Order order : orders) {
            if (order.getClient().getId().equals(clientId)) {
                clientOrders.add(order);
                return orders;
            }else {
                throw new ResourceNotFoundException("Client dont found or dont have orders.");
            }
        }
        return clientOrders;
    }
}
