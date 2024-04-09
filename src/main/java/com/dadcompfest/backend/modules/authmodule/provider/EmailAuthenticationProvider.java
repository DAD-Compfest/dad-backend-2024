package com.dadcompfest.backend.modules.authmodule.provider;

import com.dadcompfest.backend.modules.authmodule.model.EmailVerificationPOJO;
import com.dadcompfest.backend.common.provider.RedisSessionProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class EmailAuthenticationProvider {
    @Autowired
    RedisSessionProvider redisProvider;

    public String createEmailAuthenticationToken(String email) {
        String token = UUID.randomUUID().toString().substring(0, 6);
        String key = redisProvider.wrapperEmailTokenKey(email);
        try {
            redisProvider.getRedisTemplate().opsForValue().set(key,
                    redisProvider.getObjectMapper().writeValueAsString
                            (new EmailVerificationPOJO(token)),
                    RedisSessionProvider.getEmailExpirationTimeMs(), TimeUnit.MILLISECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return token;
    }

    public boolean isEmailVerified(String email) {
        String key = redisProvider.wrapperEmailTokenKey(email);
        String data = redisProvider.get(key);
        System.out.println("-----------------------========================");
        System.out.println(data);
        if (data == null) {
            return false;
        }
        try {
            EmailVerificationPOJO verificationObject =
                    redisProvider.getObjectMapper().readValue(data, EmailVerificationPOJO.class);
            return verificationObject.isAuthenticated();
        } catch (Exception err) {
            System.out.println(err);
            return false;
        }
    }

    public boolean verifyToken(String email, String token) {
        System.out.println(token);

        String key = redisProvider.wrapperEmailTokenKey(email);
        System.out.println(key);
        String data = redisProvider.get(key);
        System.out.println(data);
        if (data == null) {
            return false;
        }
        try {
            EmailVerificationPOJO verificationObject =
                    redisProvider.getObjectMapper().readValue(data, EmailVerificationPOJO.class);
            System.out.println(token);
            System.out.println(verificationObject.getToken());
            System.out.println(token.equals(verificationObject.getToken()));
            System.out.println("--------------------------------");
            if (verificationObject.getToken().equals(token)) {

                verificationObject.setAuthenticated(true);
                redisProvider.getRedisTemplate().opsForValue().set(key,
                        redisProvider.getObjectMapper().writeValueAsString(verificationObject),
                        RedisSessionProvider.getEmailExpirationTimeMs(), TimeUnit.MILLISECONDS);
                return true;
            }
            return false;
        } catch (Exception err) {
            System.out.println(err);
            return false;
        }
    }
}
