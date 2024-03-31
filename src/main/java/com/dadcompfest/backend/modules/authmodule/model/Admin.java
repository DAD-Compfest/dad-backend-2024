package com.dadcompfest.backend.modules.authmodule.model;

import com.dadcompfest.backend.modules.authmodule.provider.AuthProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Admin{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private  String email;

    public void setPassword(String password) {
        this.password = AuthProvider.getInstance().encode(password);
    }
    public  void setRawPassword(String password){
        this.password = password;
    }
}
