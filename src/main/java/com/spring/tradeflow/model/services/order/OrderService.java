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
        Client client = clientRepository.findById(order.getClient().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        order.setClient(client);

        order.getOrderItems().forEach(item -> {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            item.setProduct(product);
            item.setOrder(order);
        });

        return orderRepository.save(order);
    }

    @Transactional
    public Order addProducts(Long orderId, List<Map<String, Object>> items) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        for (Map<String, Object> item : items) {
            Long productId = ((Number) item.get("productId")).longValue();
            Integer quantity = (Integer) item.get("quantity");

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }

            OrderItem orderItem = new OrderItem(product, quantity, order);
            order.getOrderItems().add(orderItem);
        }

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
    public void removeProduct(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found"));

        order.getOrderItems().remove(orderItem);

        orderRepository.save(order);
    }

    @Transactional
    public void removeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    @Transactional
    public Order changeQuantity(Long orderId, Long orderItemId, Integer quantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found"));

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        orderItem.setQuantity(quantity);
        return orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);
        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.COMPLETED) {
            throw new InvalidDataException("Order is already completed or cancelled.");
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
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

    @Transactional
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

}
