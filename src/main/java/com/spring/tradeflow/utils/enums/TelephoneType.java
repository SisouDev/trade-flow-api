package com.spring.tradeflow.utils.enums;

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
