package com.dadcompfest.backend.modules.common.util;

import com.dadcompfest.backend.modules.authmodule.model.Admin;
import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.modules.authmodule.provider.JwtProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class AuthResponseUtil {
    public static ResponseEntity<Object> generateTeamLoginResponse(Team team, JwtProvider jwtProvider, HttpServletResponse response) throws JsonProcessingException {
        if(team == null){
            return ResponseHandler.generateResponse("Maaf username atau password tidak sesuai",
                    HttpStatus.UNAUTHORIZED, new HashMap<>());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("team", team);
        Cookie cookie = new Cookie("jwt", jwtProvider.createAuthenticationTokenForTeam(team));
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseHandler.generateResponse("Login sebagai Tim berhasil", HttpStatus.ACCEPTED, data);
    }

    public static ResponseEntity<Object> generateAdminLoginResponse(Admin admin, JwtProvider jwtProvider, HttpServletResponse response) throws JsonProcessingException {
        if(admin == null){
            return ResponseHandler.generateResponse("Maaf username atau password tidak sesuai",
                    HttpStatus.UNAUTHORIZED, new HashMap<>());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("admin", admin);
        Cookie cookie = new Cookie("jwt", jwtProvider.createAuthenticationTokenForAdmin(admin));
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseHandler.generateResponse("Login sebagai Admin berhasil", HttpStatus.ACCEPTED, data);
    }
}
