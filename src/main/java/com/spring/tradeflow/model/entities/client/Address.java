package com.spring.tradeflow.model.entities.client;

import com.spring.tradeflow.utils.enums.client.States;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Entity
@Table
@Getter
@NoArgsConstructor
@ToString
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Setter
    @Column(nullable = false)
    @NotBlank(message = "Street cannot be blank.")
    private String street;

    @Setter
    @Column(nullable = false)
    @NotBlank(message = "City cannot be blank.")
    private String city;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private States state;


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

}
