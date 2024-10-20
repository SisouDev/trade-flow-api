package com.spring.tradeflow.model.services.order;

import com.spring.tradeflow.exceptions.InvalidDataException;
import com.spring.tradeflow.exceptions.ResourceNotFoundException;
import com.spring.tradeflow.model.entities.client.Client;
import com.spring.tradeflow.model.entities.order.Coupon;
import com.spring.tradeflow.model.entities.order.Order;
import com.spring.tradeflow.model.entities.order.OrderItem;
import com.spring.tradeflow.model.entities.product.Product;
import com.spring.tradeflow.model.repositories.client.ClientRepository;
import com.spring.tradeflow.model.repositories.order.CouponRepository;
import com.spring.tradeflow.model.repositories.order.OrderRepository;
import com.spring.tradeflow.model.repositories.product.ProductRepository;
import com.spring.tradeflow.utils.enums.order.DiscountType;
import com.spring.tradeflow.utils.enums.order.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    public final OrderRepository orderRepository;
    public final ProductRepository productRepository;
    public final ClientRepository clientRepository;
    public final CouponRepository couponRepository;

    @Autowired
    public OrderService(final OrderRepository orderRepository, final ProductRepository productRepository, final ClientRepository clientRepository, final CouponRepository couponRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.couponRepository = couponRepository;
    }

    @Transactional
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

    @Transactional
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

        order.getOrderItems().addAll(orderItems);
        return orderRepository.save(order);
    }

    @Transactional
    public int updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(status);
        return orderRepository.updateOrderStatus(orderId, status);
    }

    public List<Order> getOrdersByClientId(Long clientId) {
        List<Order> clientOrders = new ArrayList<>();
        List<Order> orders = (List<Order>) orderRepository.findAll();

        for (Order order : orders) {
            if (order.getClient().getId().equals(clientId)) {
                clientOrders.add(order);
            }
        }
        if (clientOrders.isEmpty()) {
            throw new ResourceNotFoundException("Client not found or doesn't have orders.");
        }

        return clientOrders;
    }

    @Transactional
    public Order removeProduct(Long orderId, Long productId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        order.getOrderItems().removeIf(orderItem -> orderItem.getProduct().getId().equals(productId));
        return orderRepository.save(order);
    }

    @Transactional
    public void removeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    @Transactional
    public Order changeQuantity(Long orderId, Long productId, Integer quantity) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in order"));
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        orderItem.setQuantity(quantity);
        return orderRepository.save(order);
    }

    @Transactional
    public int cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getStatus() != OrderStatus.CANCELLED && order.getStatus() != OrderStatus.COMPLETED) {
            order.setStatus(OrderStatus.CANCELLED);
        } else {
            throw new InvalidDataException("Order is already completed or cancelled.");
        }
        return orderRepository.cancelOrder(orderId);
    }

    public Order applyDiscountWithCoupon(Long orderId, String couponCode) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
        if (!coupon.isValid()) {
            throw new IllegalArgumentException("Coupon is invalid or expired");
        }
        Double discountAmount;
        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            discountAmount = order.getTotalOrderPrice() * (coupon.getDiscountValue() / 100);
        } else {
            discountAmount = coupon.getDiscountValue();
        }
        Double newTotalPrice = order.getTotalOrderPrice() - discountAmount;
        order.setDiscountedPrice(newTotalPrice);
        coupon.setIsUsed(true);
        couponRepository.save(coupon);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        return orderRepository.findAllByOrderDateBetween(startDate, endDate);
    }

    public List<Order> findOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public Double calculateTotalPrice(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return order.getTotalOrderPrice();
    }

    @Transactional
    public Order updateOrderShippingPrice(Long orderId, Double shippingPrice) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setShippingPrice(shippingPrice);
        return orderRepository.save(order);
    }

}
