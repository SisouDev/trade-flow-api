package com.spring.tradeflow.model.repositories.order;

import com.spring.tradeflow.model.entities.order.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String code);
    List<Coupon> findByExpirationDateBefore(LocalDate date);
}
