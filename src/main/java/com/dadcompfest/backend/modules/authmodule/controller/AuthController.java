package com.dadcompfest.backend.modules.authmodule.controller;

import com.dadcompfest.backend.modules.authmodule.model.Admin;
import com.dadcompfest.backend.modules.authmodule.model.RegistrationRequestPOJO;
import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.modules.authmodule.provider.AuthProvider;
import com.dadcompfest.backend.modules.authmodule.provider.EmailAuthenticationProvider;
import com.dadcompfest.backend.modules.authmodule.provider.JwtProvider;
import com.dadcompfest.backend.common.middleware.RedisTeamSignInMiddleware;
import com.dadcompfest.backend.common.provider.RedisProvider;
import com.dadcompfest.backend.modules.authmodule.service.EmailService;
import com.dadcompfest.backend.modules.authmodule.service.UserService;
import com.dadcompfest.backend.common.util.AuthResponseUtil;
import com.dadcompfest.backend.common.util.ResponseHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;


@RestController
@RequestMapping("/auth")
public class AuthController {

    
    @Autowired
    private UserService<Team> teamService;

    @Autowired
    private  RedisTeamSignInMiddleware signInMiddleware;

    @Autowired 
    UserService<Admin> adminService;

    @Autowired
    EmailService emailService;

    @Autowired
    private ExecutorService virtualExecutor;

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    private  JwtProvider jwtProvider;
    @Autowired private EmailAuthenticationProvider emailAuthenticationProvider;

    @Autowired private RedisProvider redisProvider;

    AuthController(){
    }

    @PostMapping("/login/admin")
    public ResponseEntity<Object> postLoginAdmin(@RequestBody JsonNode requestBody, HttpServletResponse response) {
        return loginAdmin(requestBody, response);
    }

    @PostMapping("/login/team")
    public CompletableFuture<ResponseEntity<Object>> postLoginTeam(@RequestBody JsonNode requestBody, HttpServletResponse response) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return loginTeam(requestBody, response);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }, virtualExecutor);
    }

    @PostMapping("/email/create-email-verification")
    public  CompletableFuture<ResponseEntity<Object>> postUpdateEmailToken(@RequestBody JsonNode requestBody){
        return  CompletableFuture.supplyAsync(()->{
            String email = requestBody.get("email").asText();
            String subject = requestBody.get("subject").asText();
            String content = requestBody.get("content").asText();
            String successMessage = requestBody.get("successMessage").asText();
            String token = emailAuthenticationProvider.createEmailAuthenticationToken(email);
            try {
                emailService.sendEmailVerification(email, token,
                        content,
                        subject);
            } catch (MessagingException  e) {
                throw new RuntimeException(e);
            }
            Map<String,Object> data = new HashMap<>();
            data.put("message", successMessage );
            return  ResponseHandler.generateResponse((String) data.get("message"), HttpStatus.ACCEPTED, data);
        }, virtualExecutor);
    }

    @PostMapping("/change-password/admin")
    public  CompletableFuture<ResponseEntity<Object>>
    postChangePasswordAdmin(@RequestBody RegistrationRequestPOJO<Admin> request){

        return  CompletableFuture.supplyAsync(()->{
            Map<String, Object> response = new HashMap<>();
            if(!validateAdmin(request.getUser(), request.getPasswordConfirmation(), response)){
                return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.UNAUTHORIZED, response);
            }
            adminService.changePass(request.getUser().getUsername(), request.getPasswordConfirmation());
            redisProvider.revoke(redisProvider.wrapperEmailTokenKey(request.getUser().getEmail()));
            return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.ACCEPTED, response);

        }, virtualExecutor);
    }

    @PostMapping("/change-password/team")
    public  CompletableFuture<ResponseEntity<Object>>
    postChangePasswordTeam(@RequestBody RegistrationRequestPOJO<Team> request){
        return  CompletableFuture.supplyAsync(()->{
            Map<String, Object> response = new HashMap<>();
            request.getUser().setRawPassword(AuthProvider.getInstance().encode(request.getUser().getPassword()));
            if(!validateTeam(request.getUser(), request.getPasswordConfirmation(), response)){
                return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.UNAUTHORIZED, response);
            }
            teamService.changePass(request.getUser().getTeamUsername(), request.getPasswordConfirmation());
            redisProvider.revoke(redisProvider.wrapperEmailTokenKey(request.getUser().getTeamEmail()));
            return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.ACCEPTED, response);
        }, virtualExecutor);
    }

    @PostMapping("/email/verify")
    public  CompletableFuture<ResponseEntity<Object>> postVerifyEmailToken(@RequestBody JsonNode requestBody){
        return  CompletableFuture.supplyAsync(()->{
            String email = requestBody.get("email").asText();
            String token = requestBody.get("token").asText();
            Map<String,Object> data = new HashMap<>();
            data.put("isVerified", emailAuthenticationProvider.verifyToken(email, token));
            data.put("message", "Server telah memverifikasi token apakah sesuai atau tidak" );
            return  ResponseHandler.generateResponse((String) data.get("message"), HttpStatus.ACCEPTED, data);
        }, virtualExecutor);
    }

    protected ResponseEntity<Object> loginTeam(JsonNode requestBody, HttpServletResponse response) throws JsonProcessingException {
        String username = requestBody.get("username").asText();
        String password = requestBody.get("password").asText();
        return signInMiddleware.handleAuthTeam(username, password, response,()->
        { logger.info("cache is empty"); return loginTeamViaDB(username, password, response);});
    }

    private ResponseEntity<Object> loginTeamViaDB(String username, String password, HttpServletResponse response){
        try{
            Team team = teamService.authenticateAndGet(username, password);
            redisProvider.getRedisTemplate().opsForValue()
                    .set(redisProvider.wrapperTeamGetData(team.getTeamUsername()),
                            redisProvider.getObjectMapper().writeValueAsString(team));
            return generateTeamLoginResponse(team, response);
        }
        catch (Exception err){
            Map<String, Object> errMap = new HashMap<>();
            errMap.put("message", "Kesalahan di server");
            return  ResponseHandler.generateResponse("Kesalahan di server",
                    HttpStatus.INTERNAL_SERVER_ERROR, errMap);
        }
    }

    private ResponseEntity<Object> loginAdmin(JsonNode requestBody, HttpServletResponse response){
        String username = requestBody.get("username").asText();
        String password = requestBody.get("password").asText();
        try{
            return generateAdminLoginResponse(adminService.authenticateAndGet(username, password), response);
        }
        catch (Exception err){
            Map<String, Object> errMap = new HashMap<>();
            errMap.put("message", "Kesalahan di server");
            return  ResponseHandler.generateResponse("Kesalahan di server",
                    HttpStatus.INTERNAL_SERVER_ERROR, errMap);
        }
    }

    protected  ResponseEntity<Object> generateTeamLoginResponse(Team team, HttpServletResponse response) throws JsonProcessingException {
        return AuthResponseUtil.generateTeamLoginResponse(team, jwtProvider, response);
    }

    private ResponseEntity<Object> generateAdminLoginResponse(Admin admin, HttpServletResponse response) throws JsonProcessingException {
        return AuthResponseUtil.generateAdminLoginResponse(admin, jwtProvider, response);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> postRegisterAdmin(@RequestBody RegistrationRequestPOJO<Admin> request) {
        return registerAdmin(request.getUser(), request.getPasswordConfirmation());
    }
    @PostMapping("/register/team")
    public ResponseEntity<?> postRegisterTeam(@RequestBody RegistrationRequestPOJO<Team> request) {
        Team team = request.getUser();
        team.setRawPassword(AuthProvider.getInstance().encode(team.getPassword()));
        return registerTeam(team, request.getPasswordConfirmation());
    }

    public ResponseEntity<?> registerAdmin(Admin admin, String passwordConfirmation){
        Map<String, Object> response = new HashMap<>();
        if(! validateAdmin(admin, passwordConfirmation, response)){
            return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.UNAUTHORIZED, response);
        }
        adminService.create(admin);
        redisProvider.revoke(redisProvider.wrapperEmailTokenKey(admin.getEmail()));
        return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.ACCEPTED, response);
    }

    public ResponseEntity<?> registerTeam(Team team, String passwordConfirmation){
        Map<String, Object> response = new HashMap<>();
        if(! validateTeam(team, passwordConfirmation, response)){
            return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.UNAUTHORIZED, response);
        }
        teamService.create(team);
        redisProvider.revoke(redisProvider.wrapperEmailTokenKey(team.getTeamEmail()));
        return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.ACCEPTED, response);

    }

    public boolean validateTeam(Team team, String passwordConfirmation, Map<String,Object> response){
        if (!AuthProvider.getInstance().matches(passwordConfirmation, team.getPassword())) {
            response.put("message", "Konfirmasi password tidak sesuai");
            return false;
        }
        if(!emailAuthenticationProvider.isEmailVerified(team.getTeamEmail())){
            response.put("message", "Email belum diverifikasi");
            return  false;
        }
        response.put("message", "Berhasil memverifikasi tim "+ team.getTeamUsername());
        return true;
    }

    public boolean validateAdmin(Admin admin, String passwordConfirmation, Map<String,Object> response){
        if (!AuthProvider.getInstance().matches(passwordConfirmation, admin.getPassword())) {
            response.put("message", "Konfirmasi password tidak sesuai");
            return false;
        }
        if(!emailAuthenticationProvider.isEmailVerified(admin.getEmail())){
            response.put("message", "Email belum diverifikasi");
            return false;
        }
        response.put("message", "Berhasil memverifikasi Admin "+ admin.getUsername());
        return true;
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "jwt", required = false) String token, HttpServletResponse servletResponse) {
        Map<String, Object> response = new HashMap<>();
        generateLogoutResponse(token, response, servletResponse);
        return ResponseHandler.generateResponse((String) response.get("message"),
                (HttpStatus) response.get("status"), response);
    }

    private void generateLogoutResponse(String token, Map<String, Object> response, HttpServletResponse servletResponse) {
        if (token == null) {
            response.put("message", "Token tidak ditemukan.");
            response.put("status", HttpStatus.BAD_REQUEST);
        } else {
            redisProvider.revoke(token);
            servletResponse.addHeader("Set-Cookie", "jwt=; HttpOnly; SameSite=None; Secure; Path=/; Max-Age=0");
            response.put("message", "Berhasil logout");
            response.put("status", HttpStatus.ACCEPTED);
        }
    }              

}

