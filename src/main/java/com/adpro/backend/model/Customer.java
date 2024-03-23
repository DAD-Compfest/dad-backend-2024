package com.adpro.backend.model;

import com.adpro.backend.type.UserType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer extends AbstractUser {
    private String name;
    private String phoneNumber;

    public Customer(String username, String password, String email, String name, String phoneNumber){
        super(username, password, email, UserType.CUSTOMER.getUserType());
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
