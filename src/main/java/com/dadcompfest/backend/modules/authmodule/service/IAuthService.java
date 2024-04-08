package com.dadcompfest.backend.modules.authmodule.service;


public interface IAuthService<T> {
    T authenticateAndGet(String username, String password);
}
