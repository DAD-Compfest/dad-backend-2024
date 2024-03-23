package com.adpro.backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {
    private String username;
    private String password;
    private String email;
    private String role;
    private String name;
    private String phoneNumber;

    public Customer(String username, String password, String email, String name, String phoneNumber){
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = "CUSTOMER";
    }

    public boolean authenticate(String password){
        return this.password.equals(password);
    }

}
