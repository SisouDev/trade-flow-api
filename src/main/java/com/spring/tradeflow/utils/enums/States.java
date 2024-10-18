package com.spring.tradeflow.utils.enums;

import lombok.Getter;

@Getter
public enum States {
    AC("Acre"),
    AL("Alagoas"),
    AP("Amapá"),
    AM("Amazonas"),
    BA("Bahia"),
    CE("Ceara"),
    DF("Distrito Federal"),
    ES("Espirito Santo"),
    GO("Goias"),
    MA("Maranhão"),
    MT("Mato Grosso"),
    MS("Mato Grosso do Sul"),
    MG("Minas Gerais"),
    PA("Para"),
    PB("Paraiba"),
    PR("Parana"),
    PE("Pernambuco"),
    PI("Piaui"),
    RJ("Rio de Janeiro"),
    RN("Rio Grande do Norte"),
    RS("Rio Grande do Sul"),
    RO("Rondonia"),
    RR("Roraima"),
    SC("Santa Catarina"),
    SP("Sao Paulo"),
    SE("Sergipe"),
    TO("Tocantins");


    private final String name;

    States(String name) {
        this.name = name;
    }

}
