package com.dadcompfest.backend.modules.authmodule.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailVerificationPOJO {
    private String token;
    private boolean isAuthenticated;

    // Konstruktor tanpa argumen (default constructor)
    public EmailVerificationPOJO() {
    }

    public EmailVerificationPOJO(String token) {
        this.token = token;
        this.isAuthenticated = false;
    }
}

