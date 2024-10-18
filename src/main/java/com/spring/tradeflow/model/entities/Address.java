package com.spring.tradeflow.model.entities;

import com.spring.tradeflow.utils.enums.States;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table
@Getter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Setter
    @Column(nullable = false)
    private String street;

    @Setter
    @Column(nullable = false)
    private String city;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private States state;

    public Address(){
    }

    public Address(String city, States state, String street) {
        this.city = city;
        this.state = state;
        this.street = street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address address)) return false;
        return Objects.equals(getAddressId(), address.getAddressId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getAddressId());
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state=" + state +
                '}';
    }
}
