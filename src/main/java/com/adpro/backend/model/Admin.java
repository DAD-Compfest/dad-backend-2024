package com.adpro.backend.model;

import com.adpro.backend.type.UserType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Admin extends AbstractUser {
    public Admin(String username, String password, String email){
        super(username, password, email, UserType.ADMIN.getUserType());
    }
}
