package com.spring.tradeflow.utils.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("Pending"),
    PROCESSING("Processing"),
    APPROVED("Approved"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }
}
