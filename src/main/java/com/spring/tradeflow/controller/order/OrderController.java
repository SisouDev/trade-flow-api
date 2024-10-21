package com.spring.tradeflow.controller.order;

import com.spring.tradeflow.model.entities.order.Order;
import com.spring.tradeflow.model.entities.order.OrderItem;
import com.spring.tradeflow.model.services.order.OrderService;
import com.spring.tradeflow.utils.enums.order.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        orderService.updateOrderStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/apply-coupon")
    public ResponseEntity<Order> applyCoupon(@PathVariable Long id, @RequestParam String couponCode) {
        Order updatedOrder = orderService.applyDiscountWithCoupon(id, couponCode);
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/{id}/add-item")
    public ResponseEntity<Order> addOrderItem(@PathVariable Long id, @RequestBody List<OrderItem> items) {
        Order updatedOrder = orderService.addProducts(id, items);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}/remove-item/{itemId}")
    public ResponseEntity<Void> removeOrderItem(@PathVariable Long id, @PathVariable Long itemId) {
        orderService.removeProduct(id, itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Order>> getOrdersByClientId(@PathVariable Long clientId) {
        List<Order> orders = orderService.getOrdersByClientId(clientId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> findOrdersByStatus(@PathVariable OrderStatus status) {
        List<Order> orders = orderService.findOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Order>> getOrdersByDateRange(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<Order> orders = orderService.getOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    @PutMapping(path = "{id}/update-item/{orderItemId}")
    public ResponseEntity<Order> updateOrderItemQuantity(@PathVariable("id") Long orderItemId, @PathVariable("orderItemId") Long productId, Integer quantity) {
        Order order = orderService.getOrderById(orderItemId);
        return ResponseEntity.ok(order);
    }

}
