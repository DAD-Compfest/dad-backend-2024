package com.adpro.backend.modules.AuthModule.model;

import com.adpro.backend.modules.AuthModule.type.UserType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Admin extends AbstractUser {
    public Admin(String username, String password, String email){
        super(username, password, email, UserType.ADMIN.getUserType());
    }
}
