package com.dadcompfest.backend.modules.authmodule.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequestPOJO<T> {
    private T user;
    private String passwordConfirmation;
}
