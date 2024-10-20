package com.spring.tradeflow.controller.order;

import com.spring.tradeflow.model.entities.order.Order;
import com.spring.tradeflow.model.entities.order.OrderItem;
import com.spring.tradeflow.model.services.order.OrderService;
import com.spring.tradeflow.utils.enums.order.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @GetMapping(path = "/status/{status}")
    public List<Order> findOrderByStatus(@PathVariable("status") OrderStatus status) {
        return orderService.findOrdersByStatus(status);
    }

    @PostMapping(path = "/{id}/cancel")
    public void cancelOrder(@PathVariable("id") Long id) {
        orderService.cancelOrder(id);
    }

    @PostMapping(path = "{id}/add-item")
    public Order addItem(@PathVariable("id") Long id, @RequestBody List<OrderItem> items) {
        return orderService.addProducts(id, items);
    }

    @PutMapping(path = "{id}/update-item/{orderItemId}")
    public Order updateOrderItemQuantity(@PathVariable("id") Long orderItemId, @PathVariable("orderItemId") Long productId, Integer quantity) {
        return orderService.changeQuantity(orderItemId, productId, quantity);
    }

    @GetMapping(path = {"/{id}"})
    public List<Order> getOrdersByClientId(@PathVariable Long id) {
        return orderService.getOrdersByClientId(id);
    }
}
