package com.dadcompfest.backend.common.provider;

import com.dadcompfest.backend.common.enums.RedisConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Getter
@Component
public class RedisProvider {

    private static final long EXPIRATION_TIME_MS = 60 * 60 * 1000;
    private  static  final  long EMAIL_EXPIRATION_TIME_MS = 5 * 60 * 1000;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;



    private final ObjectMapper objectMapper;

    public RedisProvider() {
        objectMapper = new ObjectMapper();
    }

    public static long getEmailExpirationTimeMs() {
        return EMAIL_EXPIRATION_TIME_MS;
    }

    public static long getExpirationTimeMs() {
        return EXPIRATION_TIME_MS;
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    public  String wrapperEmailTokenKey(String email){
        return wrapperKey(RedisConstants.TOKEN.getPrefix(), RedisConstants.EMAIL.getPrefix(), email);
    }

    public   String wrapperAdminAuthKey(String subject){
        return  wrapperKey(RedisConstants.TOKEN.getPrefix(),
                RedisConstants.AUTH.getPrefix(), RedisConstants.ADMIN.getPrefix(), subject);
    }

    public  String wrapperTeamAuthKey(String subject){
        return  wrapperKey(RedisConstants.TOKEN.getPrefix(),
                RedisConstants.AUTH.getPrefix(), RedisConstants.TEAM.getPrefix(), subject);
    }

    public  String wrapperTeamGetData(String username){
        return  wrapperKey(RedisConstants.TEAM.getPrefix(), RedisConstants.DATA.getPrefix(), username);
    }

    public String wrapperKey(String... parts) {
        StringBuilder keyBuilder = new StringBuilder();
        for (String part : parts) {
            keyBuilder.append(part);
        }
        return keyBuilder.toString();
    }


    public void revoke(String key) {
        redisTemplate.delete(key);
    }
    public void updateTTL(String key, long newExpirationTimeMS) {
        redisTemplate.expire(key, newExpirationTimeMS, TimeUnit.MILLISECONDS);
    }
}
