package com.spring.tradeflow.model.entities.client;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
public class Admin extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String userName;

    public Admin(){
        super();
    }

    public Admin(String email, String firstName, String lastName, String password, String userName) {
        super(email, firstName, lastName, password);
        this.userName = userName;
    }
}
