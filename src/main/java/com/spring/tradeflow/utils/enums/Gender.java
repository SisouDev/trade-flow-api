package com.spring.tradeflow.utils.enums;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }
}
