package com.dadcompfest.backend.modules.authmodule.service;


public interface IAuthService<T> {
    T authenticateAndGet(String username, String password);
    String createJwtToken(String key);
    void logout(String jwt);
    boolean isJwtTokenValid(String jwt);
    String getDataFromJwt(String jwt);
}
