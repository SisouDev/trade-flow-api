package com.spring.tradeflow.model.entities.client;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.spring.tradeflow.utils.enums.client.TelephoneType;
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
public class Telephone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long telephoneId;

    @Setter
    @Column(nullable = false)
    @NotBlank(message = "Area code cannot be blank.")
    private String areaCode;

    @Setter
    @Column(nullable = false)
    @NotBlank(message = "Number cannot be blank.")
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

}
