package com.adpro.backend.model;

import com.adpro.backend.type.UserType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Admin {
    private String username;
    private String password;
    private String email;
    private String role;

    public Admin(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = UserType.ADMIN.getUserType();
    }
    public boolean authenticate(String password){
        return this.password.equals(password);
    }

}
