package com.dadcompfest.backend.modules.common.util;

import com.dadcompfest.backend.modules.authmodule.model.Admin;
import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.modules.authmodule.provider.JwtProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class AuthResponseUtil {
    public static ResponseEntity<Object> generateTeamLoginResponse(Team team, JwtProvider jwtProvider) throws JsonProcessingException {
        if(team == null){
            return ResponseHandler.generateResponse("Maaf username atau password tidak sesuai",
                    HttpStatus.UNAUTHORIZED, new HashMap<>());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("team", team);
        data.put("teamToken", jwtProvider.createAuthenticationTokenForTeam(team));
        return ResponseHandler.generateResponse("Login sebagai Tim berhasil", HttpStatus.ACCEPTED, data);
    }

    public static ResponseEntity<Object> generateAdminLoginResponse(Admin admin, JwtProvider jwtProvider) throws JsonProcessingException {
        if(admin == null){
            return ResponseHandler.generateResponse("Maaf username atau password tidak sesuai",
                    HttpStatus.UNAUTHORIZED, new HashMap<>());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("admin", admin);
        data.put("adminToken", jwtProvider.createAuthenticationTokenForAdmin(admin));

        return ResponseHandler.generateResponse("Login sebagai Admin berhasil", HttpStatus.ACCEPTED, data);
    }
}
