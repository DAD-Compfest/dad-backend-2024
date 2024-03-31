package com.dadcompfest.backend.modules.authmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dadcompfest.backend.modules.authmodule.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long>{
    Admin findByUsername(String username);
}
