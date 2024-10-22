package com.spring.tradeflow.model.entities.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.spring.tradeflow.utils.enums.client.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Getter
public class Client extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    @NotBlank(message = "Date of birth cannot be blank.")
    private LocalDate birthday;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    List<Telephone> telephones = new ArrayList<>();

    @Setter
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "address_id", referencedColumnName = "addressId")
    private Address address;

    public Client() {
        super();
    }

    public Client(String email, String firstName, String lastName, String password, Address address, LocalDate birthday, Gender gender, List<Telephone> telephones) {
        super(email, firstName, lastName, password);
        this.address = address;
        this.birthday = birthday;
        this.gender = gender;
        this.telephones = telephones;
    }

    public void addTelephone(Telephone telephone) {
        telephones.add(telephone);
        telephone.setClient(this);
    }

    public void removeTelephone(Telephone telephone) {
        telephones.remove(telephone);
        telephone.setClient(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;
        return Objects.equals(getId(), client.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return String.format(
                "Client {\n" +
                        "  Client Address City: %s,\n" +
                        "  Client Address State: %s,\n" +
                        "  Client Id: %d,\n" +
                        "  Client Birthday: %s,\n" +
                        "  Client Gender: %s,\n" +
                        "  Client Telephones Size: %s\n" +
                        "}",
                address.getCity(), address.getState().getName(), id, birthday, gender.getGender(), telephones.size()
        );
    }
}
