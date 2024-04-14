package com.dadcompfest.backend.modules.authmodule.service;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dadcompfest.backend.modules.authmodule.model.Admin;
import com.dadcompfest.backend.modules.authmodule.provider.AuthProvider;
import com.dadcompfest.backend.modules.authmodule.repository.AdminRepository;

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
    public Admin changePass(String username, String newPassword) {
        Admin admin =  adminRepository.findByUsername(username);
        if(admin == null){
            return  null;
        }
        admin.setPassword(newPassword);
        adminRepository.save(admin);
        return admin;
    }

    @Transactional
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

    @Transactional
    @Override
    public Admin authenticateAndGet(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        if(admin != null && AuthProvider.getInstance().matches(password, admin.getPassword())){
            return admin;
        }
        return null;
    }
    
}
