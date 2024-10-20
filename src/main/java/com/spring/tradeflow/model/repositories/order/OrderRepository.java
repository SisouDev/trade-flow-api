package com.spring.tradeflow.model.repositories.order;

import com.spring.tradeflow.model.entities.order.Order;
import com.spring.tradeflow.model.entities.order.OrderItem;
import com.spring.tradeflow.utils.enums.order.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    List<Order> findByClientId(Long clientId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByShippingPriceGreaterThan(Double shippingPrice);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :orderId")
    int updateOrderStatus(Long orderId, OrderStatus status);

    @Transactional
    default void addOrderItemsToOrder(Order order, List<OrderItem> orderItems) {
        order.getOrderItems().addAll(orderItems);
        save(order);
    }

    @Modifying
    @Transactional
    @Query("DELETE FROM OrderItem oi WHERE oi.order.id = :orderId AND oi.product.id = :productId AND oi.id = :orderItemId")
    void removeProductFromOrderItems(Long orderId, Long productId, Long orderItemId);

    @Modifying
    @Transactional
    @Query("UPDATE OrderItem oi SET oi.quantity = :quantity WHERE oi.id = :orderItemId AND oi.product.id = :productId")
    int changeQuantity(Long orderItemId, Integer quantity, Long productId);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = 'CANCELLED' WHERE o.id = :orderId")
    int cancelOrder(Long orderId);
}
