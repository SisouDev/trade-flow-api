package com.spring.tradeflow.model.entities.order;

import com.spring.tradeflow.utils.enums.order.DiscountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(nullable = false)
    private Double discountValue;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private Boolean isUsed = false;

    public boolean isValid() {
        return !isUsed && expirationDate.isAfter(LocalDate.now());
    }

}
