package com.adpro.backend.modules.AuthModule.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractUser {
    private String username;
    private String password;
    private String email;
    @Setter(AccessLevel.PRIVATE)
    private String role;

    public AbstractUser(String username, String password, String email, String role){
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public boolean authenticate(String password){
        return this.password.equals(password);
    }
}
