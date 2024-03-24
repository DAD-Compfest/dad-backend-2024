package com.adpro.backend.modules.authmodule.repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.adpro.backend.modules.authmodule.model.AbstractUser;

@Repository
public class UserRepository<T extends AbstractUser> {
    private Map<String, T> userMap = new HashMap<>();

    public T findByUsername(String username) {
        return userMap.get(username);
    }

    public List<T> findAll() {
        return new ArrayList<>(userMap.values());
    }

    public T update(T user) {
        if (user == null) {
            throw new NullPointerException("User cannot be null");
        }
        
        if (!userMap.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("User does not exist in the user map");
        }
        
        userMap.put(user.getUsername(), user);
        return user;
    }
    public void delete(String username) {
        if (!userMap.containsKey(username)) {
            throw new IllegalArgumentException("User with the specified username does not exist");
        }
        
        userMap.remove(username);
    }

    public T add(T user) {
        if (user == null) {
            throw new NullPointerException("User cannot be null");
        }
        
        if (userMap.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("Username must be unique");
        }
        
        userMap.put(user.getUsername(), user);
        return user;
    }
}

