package com.spring.tradeflow.utils.enums.client;

import lombok.Getter;

@Getter
public enum TelephoneType {
    RESIDENTIAL("Residential"),
    MOBILE("Mobile"),
    WORK("Work"),
    OTHER("Other");

    private final String value;

    TelephoneType(String value) {
        this.value = value;
    }

}
