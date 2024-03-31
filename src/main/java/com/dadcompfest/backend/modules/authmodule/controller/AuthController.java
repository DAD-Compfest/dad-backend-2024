package com.dadcompfest.backend.modules.authmodule.controller;

import com.dadcompfest.backend.modules.authmodule.model.Admin;
import com.dadcompfest.backend.modules.authmodule.model.RegistrationRequestPOJO;
import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.modules.authmodule.provider.AuthProvider;
import com.dadcompfest.backend.modules.authmodule.provider.JwtProvider;
import com.dadcompfest.backend.modules.authmodule.provider.EmailTokenProvider;
import com.dadcompfest.backend.modules.authmodule.service.EmailService;
import com.dadcompfest.backend.modules.authmodule.service.UserService;
import com.dadcompfest.backend.modules.commonmodule.util.ResponseHandler;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    
    @Autowired
    private UserService<Team> teamService;

    @Autowired 
    UserService<Admin> adminService;

    @Autowired
    EmailService emailService;

    AuthController(){
    }

    @PostMapping("/login/admin")
    public ResponseEntity<Object> postLoginAdmin(@RequestBody JsonNode requestBody) {
        return loginAdmin(requestBody);
    }

    @PostMapping("/login/team")
    public CompletableFuture<ResponseEntity<Object>> postLoginTeam(@RequestBody JsonNode requestBody) {
        return CompletableFuture.supplyAsync(() -> {
            return loginTeam(requestBody);
        }, threadPoolTaskExecutor);
    }
    @PostMapping("/email/create-email-verification")
    public  CompletableFuture<ResponseEntity<Object>> postUpdateEmailToken(@RequestBody JsonNode requestBody){
        return  CompletableFuture.supplyAsync(()->{
            String email = requestBody.get("email").asText();
            String subject = requestBody.get("subject").asText();
            String content = requestBody.get("content").asText();
            String successMessage = requestBody.get("successMessage").asText();
            String token = EmailTokenProvider.getInstance().generateToken(email);
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
        }, threadPoolTaskExecutor);
    }

    @PostMapping("/change-password/admin")
    public  CompletableFuture<ResponseEntity<Object>>
    postChangePasswordAdmin(@RequestBody RegistrationRequestPOJO<Admin> request){

        return  CompletableFuture.supplyAsync(()->{
            Map<String, Object> response = new HashMap<>();
            if(!validateAdmin(request.getUser(), request.getPasswordConfirmation(), response)){
                return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.UNAUTHORIZED, response);
            }
            adminService.createOrUpdate(request.getUser());
            EmailTokenProvider.getInstance().removeToken(request.getUser().getEmail());// remove token after register success
            return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.ACCEPTED, response);

        }, threadPoolTaskExecutor);
    }

    @PostMapping("/change-password/team")
    public  CompletableFuture<ResponseEntity<Object>>
    postChangePasswordTeam(@RequestBody RegistrationRequestPOJO<Team> request){
        return  CompletableFuture.supplyAsync(()->{
            Map<String, Object> response = new HashMap<>();
            if(!validateTeam(request.getUser(), request.getPasswordConfirmation(), response)){
                return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.UNAUTHORIZED, response);
            }
            teamService.createOrUpdate(request.getUser());
            EmailTokenProvider.getInstance().removeToken(request.getUser().getTeamEmail());// remove token after register success
            return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.ACCEPTED, response);
        }, threadPoolTaskExecutor);
    }

    @PostMapping("/email/verify")
    public  CompletableFuture<ResponseEntity<Object>> postVerifyEmailToken(@RequestBody JsonNode requestBody){
        return  CompletableFuture.supplyAsync(()->{
            String email = requestBody.get("email").asText();
            String token = requestBody.get("token").asText();
            Map<String,Object> data = new HashMap<>();
            data.put("isVerified", EmailTokenProvider.getInstance().verifyToken(email, token));
            data.put("message", "Server telah memverifikasi token apakah sesuai atau tidak" );
            return  ResponseHandler.generateResponse((String) data.get("message"), HttpStatus.ACCEPTED, data);
        }, threadPoolTaskExecutor);
    }


    private ResponseEntity<Object> loginTeam(JsonNode requestBody){
        String username = requestBody.get("username").asText();
        String password = requestBody.get("password").asText();
        return generateTeamLoginResponse(teamService.authenticateAndGet(username, password));
    }

    private ResponseEntity<Object> loginAdmin(JsonNode requestBody){
        String username = requestBody.get("username").asText();
        String password = requestBody.get("password").asText();
        Admin admin = adminService.authenticateAndGet(username, password);
        return generateAdminLoginResponse(admin);
    }

    private ResponseEntity<Object> generateTeamLoginResponse(Team team){
        if(team == null){
            return ResponseHandler.generateResponse("Maaf username atau password tidak sesuai", 
            HttpStatus.UNAUTHORIZED, new HashMap<>());
        }
        Map<String, Object> data = new HashMap<>();
        Object teamData = team;
        data.put("team", teamData);
        data.put("teamToken", JwtProvider.getInstance().createJwtToken(team.getTeamUsername()));
        return ResponseHandler.generateResponse("Login sebagai Tim berhasil", HttpStatus.ACCEPTED, data);
    }

    private ResponseEntity<Object> generateAdminLoginResponse(Admin admin){
        if(admin == null){
            return ResponseHandler.generateResponse("Maaf username atau password tidak sesuai", 
            HttpStatus.UNAUTHORIZED, new HashMap<>());
        }
        Map<String, Object> data = new HashMap<>();
        Object adminData = admin;
        data.put("admin", adminData);
        data.put("adminToken", JwtProvider.getInstance().createJwtToken(admin.getUsername()));

        return ResponseHandler.generateResponse("Login sebagai Admin berhasil", HttpStatus.ACCEPTED, data);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> postRegisterAdmin(@RequestBody RegistrationRequestPOJO<Admin> request) {
        return registerAdmin(request.getUser(), request.getPasswordConfirmation());
    }
    @PostMapping("/register/team")
    public ResponseEntity<?> postRegisterTeam(@RequestBody RegistrationRequestPOJO<Team> request) {
        return registerTeam(request.getUser(), request.getPasswordConfirmation());
    }

    public ResponseEntity<?> registerAdmin(Admin admin, String passwordConfirmation){
        Map<String, Object> response = new HashMap<>();
        if(! validateAdmin(admin, passwordConfirmation, response)){
            return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.UNAUTHORIZED, response);
        }
        adminService.create(admin);
        EmailTokenProvider.getInstance().removeToken(admin.getEmail());// remove token after register success
        return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.ACCEPTED, response);
    }

    public ResponseEntity<?> registerTeam(Team team, String passwordConfirmation){
        Map<String, Object> response = new HashMap<>();
        if(! validateTeam(team, passwordConfirmation, response)){
            return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.UNAUTHORIZED, response);
        }
        teamService.create(team);
        EmailTokenProvider.getInstance().removeToken(team.getTeamEmail());// remove token after register success
        return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.ACCEPTED, response);

    }

    public boolean validateTeam(Team team, String passwordConfirmation, Map<String,Object> response){
        if (!AuthProvider.getInstance().matches(passwordConfirmation, team.getPassword())) {
            response.put("message", "Konfirmasi password tidak sesuai");
            return false;
        }
        if(!EmailTokenProvider.getInstance().isVerified(team.getTeamEmail())){
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
        if(!EmailTokenProvider.getInstance().isVerified(admin.getEmail())){
            response.put("message", "Email belum diverifikasi");
            return false;
        }
        response.put("message", "Berhasil memverifikasi Admin "+ admin.getUsername());
        return true;
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        Map<String, Object> response = new HashMap<>();
        generateLogoutResponse(token, response);
        return ResponseHandler.generateResponse((String) response.get("message"),
                (HttpStatus) response.get("status"), response);
    }

    private void generateLogoutResponse(String token, Map<String, Object> response) {
        if (token == null) {
            response.put("message", "Token tidak ditemukan.");
            response.put("status", HttpStatus.BAD_REQUEST);
        } else {
            JwtProvider.getInstance().revokeJwtToken(token);
            response.put("message", "Berhasil logout");
            response.put("status", HttpStatus.ACCEPTED);
        }
    }              

}

