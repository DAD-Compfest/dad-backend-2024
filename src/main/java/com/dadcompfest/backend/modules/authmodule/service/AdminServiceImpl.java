package com.dadcompfest.backend.modules.authmodule.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dadcompfest.backend.modules.authmodule.enums.UserType;
import com.dadcompfest.backend.modules.authmodule.model.Admin;
import com.dadcompfest.backend.modules.authmodule.provider.AuthProvider;
import com.dadcompfest.backend.modules.authmodule.provider.JwtProvider;
import com.dadcompfest.backend.modules.authmodule.repository.AdminRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminServiceImpl extends UserService<Admin>{

    @Autowired
    AdminRepository adminRepository;

    @Override
    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Override
    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    @Override
    public Admin createOrUpdate(Admin newAdmin) {
        Admin admin = adminRepository.findByUsername(newAdmin.getUsername());
        if(admin != null){
            admin.setRawPassword(newAdmin.getPassword());
            return adminRepository.save(admin);
        }

        return adminRepository.save(newAdmin);
    }

    @Override
    public Admin create(Admin entity) {
        return  adminRepository.save(entity);
    }

    @Override
    public void remove(Admin entity) {
        adminRepository.delete(entity);
    }
    @Override
    public Admin authenticateAndGet(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        if(admin != null && AuthProvider.getInstance().matches(password, admin.getPassword())){
            return admin;
        }
        return null;
    }

    @Override
    public String createJwtToken(String key) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("username", key);
        hashMap.put("relation", UserType.ADMIN.getUserType());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonPayload = objectMapper.writeValueAsString(hashMap);
            return JwtProvider.getInstance().createJwtToken(jsonPayload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void logout(String jwt) {
        JwtProvider.getInstance().revokeJwtToken(jwt);
    }

    @Override
    public boolean isJwtTokenValid(String jwt) {
        return  JwtProvider.getInstance().isJwtTokenValid(jwt);
    }

    @Override
    public String getDataFromJwt(String jwt) {
        return  JwtProvider.getInstance().getDataFromJwt(jwt);
    }
    
}
