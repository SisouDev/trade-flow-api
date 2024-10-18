package com.spring.tradeflow.model.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.spring.tradeflow.utils.HashPassword;
import com.spring.tradeflow.utils.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Getter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String firstName;

    @Setter
    @Column(nullable = false)
    private String lastName;

    @Setter
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Setter
    @Column(nullable = false)
    private LocalDate birthday;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Telephone> telephones = new ArrayList<>();

    @Setter
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "address_id", referencedColumnName = "addressId")
    private Address address;

    public Client() {
    }

    public void setPassword(String password) {
        this.password = HashPassword.hashPassword(password);
    }

    public Client(String firstName, String lastName, LocalDate birthday, String email, String password,
                  Gender gender, Address address, List<Telephone> telephones
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
        setPassword(password);
        this.gender = gender;
        this.address = address;
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
        return "Client{" +
                "address=" + address +
                ", id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", birthday=" + birthday +
                ", gender=" + gender +
                ", telephones=" + telephones +
                '}';
    }
}
