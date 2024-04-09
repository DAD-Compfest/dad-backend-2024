package com.dadcompfest.backend.modules.authmodule.provider;


import com.dadcompfest.backend.modules.authmodule.model.Admin;
import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.common.provider.RedisSessionProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.concurrent.TimeUnit;

@Component
public class JwtProvider {

    @Autowired
    private RedisSessionProvider redisProvider;
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    String createJwtToken(String subject){
        Claims claims = Jwts.claims();
        claims.setSubject(subject);
        return  Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();
    }

    public String createAuthenticationTokenForTeam(Team team) throws JsonProcessingException {
        String token = createJwtToken(team.getTeamUsername());
        String teamJson = redisProvider.getObjectMapper().writeValueAsString(team);
        String key = redisProvider.wrapperTeamAuthKey(token);
        redisProvider.getRedisTemplate().opsForValue().set(key, teamJson, RedisSessionProvider.getExpirationTimeMs(), TimeUnit.MILLISECONDS);
        return key;
    }

    public  String createAuthenticationTokenForAdmin(Admin admin) throws JsonProcessingException {
        String token = createJwtToken(admin.getUsername());
        String adminJson = redisProvider.getObjectMapper().writeValueAsString(admin);
        String key = redisProvider.wrapperAdminAuthKey(token);
        redisProvider.getRedisTemplate().opsForValue().set(key, adminJson,  RedisSessionProvider.getExpirationTimeMs(), TimeUnit.MILLISECONDS);
        return key;
    }

}
