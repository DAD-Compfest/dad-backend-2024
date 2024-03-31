package com.dadcompfest.backend.modules.authmodule.provider;

import com.dadcompfest.backend.modules.authmodule.model.EmailVerificationPOJO;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EmailTokenProvider {

    private static EmailTokenProvider instance;

    private final Map<String, EmailVerificationPOJO> tokenMap;

    private EmailTokenProvider() {
        tokenMap = new HashMap<>();
    }

    public static synchronized EmailTokenProvider getInstance() {
        if (instance == null) {
            instance = new EmailTokenProvider();
        }
        return instance;
    }

    public  String generateToken(String email) {
        String token = UUID.randomUUID().toString().substring(0,6);
        tokenMap.put(email, new EmailVerificationPOJO(token));
        return token;
    }

    public  boolean verifyToken(String email, String token) {
        EmailVerificationPOJO storedToken = tokenMap.get(email);
        if(storedToken == null){
            return  false;
        }
        if(token.equals(storedToken.getToken())){
            System.out.println("I");
            storedToken.setAuthenticated(true);
        }

        return storedToken.getToken().equals(token) && storedToken.isAuthenticated();
    }

    public  boolean isVerified(String email){
        EmailVerificationPOJO pojo = tokenMap.get(email);
        System.out.println(email);
        System.out.println(tokenMap.size());
        if(pojo == null){
            System.out.println("tidak ada bro");
            return false;
        }
        else {
            System.out.println(email);
            System.out.println(pojo.getToken());
            System.out.println(pojo.isAuthenticated());
            return pojo.isAuthenticated();
        }
    }

    public  void removeToken(String email) {
        tokenMap.remove(email);
    }
}
