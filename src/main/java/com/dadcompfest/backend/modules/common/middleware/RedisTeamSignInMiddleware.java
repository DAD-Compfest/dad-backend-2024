package com.dadcompfest.backend.modules.common.middleware;

import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.modules.authmodule.provider.AuthProvider;
import com.dadcompfest.backend.modules.authmodule.provider.JwtProvider;
import com.dadcompfest.backend.modules.authmodule.service.UserService;
import com.dadcompfest.backend.modules.common.enums.RedisConstants;
import com.dadcompfest.backend.modules.common.provider.RedisProvider;
import com.dadcompfest.backend.modules.common.util.AuthResponseUtil;
import com.dadcompfest.backend.modules.common.util.ResponseHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.function.Supplier;


@Component
public class RedisTeamSignInMiddleware {

    @Autowired
    private RedisProvider redisProvider;

    @Autowired
    private JwtProvider jwtProvider;

    public ResponseEntity<Object> handleAuthTeam(String username, String password, Supplier<ResponseEntity<Object>> onFailure) throws JsonProcessingException {
        String teamString = redisProvider.get(redisProvider.wrapperTeamGetData(username));
        if(teamString != null){
            Team team = redisProvider.getObjectMapper().readValue(teamString, Team.class);
            System.out.println(team.getPassword());
            System.out.println(teamString);
            if(!AuthProvider.getInstance().matches(password, team.getPassword())) {
                team = null;
            }
            return AuthResponseUtil.generateTeamLoginResponse(team, jwtProvider);
        }
        return onFailure.get();
    }
}

