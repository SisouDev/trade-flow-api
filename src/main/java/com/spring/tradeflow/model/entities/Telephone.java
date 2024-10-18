package com.spring.tradeflow.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.spring.tradeflow.utils.enums.TelephoneType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table
@Getter
public class Telephone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long telephoneId;

    @Setter
    @Column(nullable = false)
    private String areaCode;

    @Setter
    @Column(nullable = false)
    private String number;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TelephoneType type;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @Setter
    @JsonBackReference
    private Client client;

    public Telephone(){
    }

    public Telephone(String areaCode, String number, TelephoneType type) {
        this.areaCode = areaCode;
        this.number = number;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Telephone telephone)) return false;
        return Objects.equals(getTelephoneId(), telephone.getTelephoneId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTelephoneId());
    }

    @Override
    public String toString() {
        return "Telephone{" +
                "areaCode='" + areaCode + '\'' +
                ", number='" + number + '\'' +
                ", telephoneId=" + telephoneId +
                ", type=" + type +
                '}';
    }
}
