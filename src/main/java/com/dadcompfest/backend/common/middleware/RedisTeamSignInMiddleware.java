package com.dadcompfest.backend.common.middleware;

import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.modules.authmodule.provider.AuthProvider;
import com.dadcompfest.backend.modules.authmodule.provider.JwtProvider;
import com.dadcompfest.backend.common.provider.RedisSessionProvider;
import com.dadcompfest.backend.common.util.AuthResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;


@Component
public class RedisTeamSignInMiddleware {

    @Autowired
    private RedisSessionProvider redisProvider;

    @Autowired
    private JwtProvider jwtProvider;

    public ResponseEntity<Object> handleAuthTeam(String username, String password, HttpServletResponse response, Supplier<ResponseEntity<Object>> onFailure) throws JsonProcessingException {
        String teamString = redisProvider.get(redisProvider.wrapperTeamGetData(username));
        if(teamString != null){
            Team team = redisProvider.getObjectMapper().readValue(teamString, Team.class);
            System.out.println(team.getPassword());
            System.out.println(teamString);
            if(!AuthProvider.getInstance().matches(password, team.getPassword())) {
                team = null;
            }
            return AuthResponseUtil.generateTeamLoginResponse(team, jwtProvider, response);
        }
        return onFailure.get();
    }
}

