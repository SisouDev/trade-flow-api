package com.spring.tradeflow.controller.order;

import com.spring.tradeflow.model.entities.order.Order;
import com.spring.tradeflow.model.entities.order.OrderItem;
import com.spring.tradeflow.model.services.order.OrderService;
import com.spring.tradeflow.utils.enums.order.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        log.info("Create order: {}", order);
        Order createdOrder = orderService.createOrder(order);
        log.info("Order created: {}", createdOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        log.info("Get order by id: {}", id);
        Order order = orderService.getOrderById(id);
        log.info("Get order by id ok: {}", order);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        log.info("Get all orders");
        List<Order> orders = orderService.getAllOrders();
        log.info("Get all orders ok: {}", orders);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        log.info("Update order status: {}", status);
        orderService.updateOrderStatus(id, status);
        log.info("Update order status ok: {}", status);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        log.info("Cancel order: {}", id);
        orderService.cancelOrder(id);
        log.info("Cancel order ok: {}", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/apply-coupon")
    public ResponseEntity<Order> applyCoupon(@PathVariable Long id, @RequestParam String couponCode) {
        log.info("Apply coupon: {}", couponCode);
        Order updatedOrder = orderService.applyDiscountWithCoupon(id, couponCode);
        log.info("Apply coupon ok: {}", updatedOrder);
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/{id}/add-item")
    public ResponseEntity<Order> addOrderItem(@PathVariable Long id, @RequestBody List<Map<String, Object>> items) {
        log.info("Add items: {}", items);
        Order updatedOrder = orderService.addProducts(id, items);
        log.info("Add items ok: {}", updatedOrder);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}/remove-item/{itemId}")
    public ResponseEntity<Void> removeOrderItem(@PathVariable Long id, @PathVariable Long itemId) {
        log.info("Remove item: {}", itemId);
        orderService.removeProduct(id, itemId);
        log.info("Remove item ok: {}", itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Order>> getOrdersByClientId(@PathVariable Long clientId) {
        log.info("Get orders by clientId: {}", clientId);
        List<Order> orders = orderService.getOrdersByClientId(clientId);
        log.info("Get orders by clientId ok: {}", orders);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> findOrdersByStatus(@PathVariable OrderStatus status) {
        log.info("Find orders by status: {}", status);
        List<Order> orders = orderService.findOrdersByStatus(status);
        log.info("Find orders by status ok: {}", orders);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Order>> getOrdersByDateRange(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        log.info("Get orders by date range: {} - {}", startDate, endDate);
        List<Order> orders = orderService.getOrdersByDateRange(startDate, endDate);
        log.info("Get orders by date range ok: {}", orders);
        return ResponseEntity.ok(orders);
    }

    @PutMapping(path = "{id}/update-item/{orderItemId}")
    public ResponseEntity<Order> updateOrderItemQuantity(@PathVariable("id") Long orderId,
                                                         @PathVariable("orderItemId") Long orderItemId,
                                                         @RequestParam Integer quantity) {
        log.info("Update order item quantity: {}", quantity);
        Order updatedOrder = orderService.changeQuantity(orderId, orderItemId, quantity);
        log.info("Update order item quantity ok: {}", updatedOrder);
        return ResponseEntity.ok(updatedOrder);
    }

}
