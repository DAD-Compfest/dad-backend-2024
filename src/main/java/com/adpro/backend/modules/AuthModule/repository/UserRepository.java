package com.adpro.backend.modules.authmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.adpro.backend.modules.authmodule.model.AbstractUser;

public interface UserRepository <T extends AbstractUser> extends JpaRepository<T, Long>{
    T findByUsername(String username);
    List<T> findAll();
    T update(T user);
    void delete(String username);
    T add(T user);
}
