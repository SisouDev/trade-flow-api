package com.spring.tradeflow.utils.enums.product;

import lombok.Getter;

@Getter
public enum ProductType {
    BOOKS("Books"),
    GAMES("Games"),
    MOVIES("Movies"),
    SERIES("Series"),
    ANIMES("Animes");

    private final String description;

    ProductType(String description) {
        this.description = description;
    }

}
