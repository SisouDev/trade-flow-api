package com.spring.tradeflow.model.repositories.order;

import com.spring.tradeflow.model.entities.order.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
