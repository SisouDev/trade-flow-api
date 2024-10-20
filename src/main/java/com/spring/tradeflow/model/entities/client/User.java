package com.spring.tradeflow.model.entities.client;

import com.spring.tradeflow.utils.common.HashPassword;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
public abstract class User {
    @Setter
    private String firstName;

    @Setter
    private String lastName;

    @Setter
    private String email;

    private String password;

    public User(){
    }

    public User(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        setPassword(password);
    }

    public void setPassword(String password) {
        this.password = HashPassword.hashPassword(password);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
