package com.adpro.backend.modules.authmodule.controller;

import com.adpro.backend.modules.authmodule.enums.UserType;
import com.adpro.backend.modules.authmodule.model.Admin;
import com.adpro.backend.modules.authmodule.model.Customer;
import com.adpro.backend.modules.authmodule.provider.JwtProvider;
import com.adpro.backend.modules.authmodule.service.UserService;
import com.adpro.backend.modules.commonmodule.util.ResponseHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private UserService<Admin> adminService;

    @Autowired
    private UserService<Customer> customerService;


    private final BCryptPasswordEncoder passwordEncoder;

    AuthController(){
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/login/admin")
    public ResponseEntity<Object> loginAdmin(@RequestBody JsonNode requestBody) {
        return login(requestBody, UserType.ADMIN);
    }

    @PostMapping("/login/customer")
    public ResponseEntity<Object> loginCustomer(@RequestBody JsonNode requestBody) {
        return login(requestBody, UserType.CUSTOMER);
    }

    public ResponseEntity<Object> login(JsonNode requestBody, UserType userType) {
        String username = requestBody.get("username").asText();
        String password = requestBody.get("password").asText();
        String role = userType.getUserType();

        boolean isValidAuthenticated = userType.equals(UserType.ADMIN) ?
                adminService.authenticateUser(username, passwordEncoder.encode(password)) :
                customerService.authenticateUser(username, passwordEncoder.encode(password));

        if (isValidAuthenticated) {
            Map<String, Object> objectMap = new HashMap<>();
            Object userData = userType.equals(UserType.ADMIN) ? adminService.findByUsername(username) : customerService.findByUsername(username);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMap.put("userData", userData);
            objectMap.put("token", JwtProvider.getInstance()
                    .createJwtToken(objectMapper.createObjectNode()
                            .put("username", username)
                            .put("role", role)
                            .toString()));

            HttpStatus status = HttpStatus.ACCEPTED;
            String message = userType.equals(UserType.ADMIN) ? "Login sebagai Admin berhasil" : "Login sebagai Customer berhasil";
            return ResponseHandler.generateResponse(message, status, objectMap);
        }

        return ResponseHandler.generateResponse("Gagal login", HttpStatus.UNAUTHORIZED, new HashMap<>());
    }


    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer, @RequestParam("confirmationPassword")
    String passwordConfirmation) {
        Map<String, Object> response = new HashMap<>();
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        if( !customer.isValid()){
            response.put("message", "Field customer tidak valid");
            return  ResponseHandler.
                    generateResponse((String) response.get("message"),
                            HttpStatus.UNAUTHORIZED, response);
        }
        if(!passwordEncoder.matches(passwordConfirmation, customer.getPassword())){
            response.put("message", "Konfirmasi password tidak sesuai");
            return  ResponseHandler.
                    generateResponse((String) response.get("message"),
                            HttpStatus.UNAUTHORIZED, response);
        }
        customerService.addUser(customer);
        response.put("message", "Berhasil mendaftarkan Customer");
        return ResponseHandler.generateResponse((String) response.get("message"),
                HttpStatus.ACCEPTED, response);
    }



    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin, @RequestParam("confirmationPassword")
    String passwordConfirmation) {
        Map<String, Object> response = new HashMap<>();
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        if( !admin.isValid()){
            response.put("message", "Field admin tidak valid");
            return  ResponseHandler.
                    generateResponse((String) response.get("message"),
                            HttpStatus.UNAUTHORIZED, response);
        }
        if(!passwordEncoder.matches(passwordConfirmation, admin.getPassword())){
            response.put("message", "Konfirmasi password tidak sesuai");
            return  ResponseHandler.
                    generateResponse((String) response.get("message"),
                            HttpStatus.UNAUTHORIZED, response);
        }
        adminService.addUser(admin);
        response.put("message", "Berhasil mendaftarkan admin");
        return ResponseHandler.generateResponse((String) response.get("message"),
                HttpStatus.ACCEPTED, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        if(token == null){
            response.put("message", "Token tidak ditemukan.");
            return ResponseHandler.generateResponse((String) response.get("message"),
                    HttpStatus.BAD_REQUEST, response);
        }
        JwtProvider.getInstance().revokeJwtToken(token);
        response.put("message", "Berhasil logout");
        return  ResponseHandler.generateResponse((String) response.get("message"),
                HttpStatus.ACCEPTED, response);
    }
}

