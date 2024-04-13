package com.dadcompfest.backend.common.util;

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
        String jwt = jwtProvider.createAuthenticationTokenForTeam(team);
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true); // Uncomment this line if you are using HTTPS (after deploying frontend)
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
        String jwt = jwtProvider.createAuthenticationTokenForAdmin(admin);
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true); // Uncomment this line if you are using HTTPS (after deploying frontend)
        response.addCookie(cookie);
        return ResponseHandler.generateResponse("Login sebagai Admin berhasil", HttpStatus.ACCEPTED, data);
    }
}
