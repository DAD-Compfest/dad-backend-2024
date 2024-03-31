package com.dadcompfest.backend.modules.authmodule.service;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dadcompfest.backend.modules.authmodule.enums.UserType;
import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.modules.authmodule.provider.AuthProvider;
import com.dadcompfest.backend.modules.authmodule.provider.JwtProvider;
import com.dadcompfest.backend.modules.authmodule.repository.TeamRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TeamServiceImpl extends UserService<Team>{
    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Team findByUsername(String username) {
        return  teamRepository.findByUsername(username);
    }

    @Override
    public List<Team> getAll() {
        return  teamRepository.findAll();
    }

    @Override
    public  Team authenticateAndGet(String username, String password) {
        Team team = entityManager.createQuery("SELECT u FROM Team u WHERE u.teamUsername = :username", Team.class)
                .setParameter("username", username)
                .getSingleResult();
    

        if (team == null) {
            return null;
        }

        if (AuthProvider.getInstance().matches(password, team.getPassword())) {
            return team;
        }

        return null;
    }
    @Override
    public String createJwtToken(String key) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("username", key);
        hashMap.put("relation", UserType.TEAM.getUserType());

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
    public boolean isJwtTokenValid(String jwt) {
        return  JwtProvider.getInstance().isJwtTokenValid(jwt);
    }

    @Override
    public String getDataFromJwt(String jwt) {
        return  JwtProvider.getInstance().getDataFromJwt(jwt);
    }

    @Override
    public void logout(String token ) {
        JwtProvider.getInstance().revokeJwtToken(token);
    }

    @Override
    public Team createOrUpdate(Team t) {
        Team existingTeam = teamRepository.findByUsername(t.getTeamUsername());
        if (existingTeam != null) {
            existingTeam.setTeamName(t.getTeamName());
            existingTeam.setTeamMembers(t.getTeamMembersList());
            existingTeam.setRawPassword(t.getPassword());
            return teamRepository.save(existingTeam);
        } else {
            return teamRepository.save(t);
        }
    }

    @Override
    public Team create(Team entity) {
        return  teamRepository.save(entity);
    }

    @Override
    public void remove(Team t) {
        teamRepository.delete(t);
    }

    
}
