package com.dadcompfest.backend.modules.authmodule.service;

import java.util.List;

import com.dadcompfest.backend.common.provider.RedisProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.modules.authmodule.provider.AuthProvider;
import com.dadcompfest.backend.modules.authmodule.repository.TeamRepository;

@Service
public class TeamServiceImpl extends UserService<Team>{
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    RedisProvider redisProvider;

    @Override
    public Team findByUsername(String username) {
        return  teamRepository.findByUsername(username);
    }

    @Override
    public List<Team> getAll() {
        return  teamRepository.findAll();
    }

    @Transactional
    @Override
    public  Team authenticateAndGet(String username, String password) {
        try{
            Team team = teamRepository.findByUsername(username);
            if (team == null) {
                return null;
            }
            if (AuthProvider.getInstance().matches(password, team.getPassword())) {
                return team;
            }
            return null;
        }catch (Exception err){
            return  null;
        }
    }

    @Transactional
    @Override
    public Team createOrUpdate(Team t) {
        try {
            Team existingTeam = teamRepository.findByUsername(t.getTeamUsername());
            if (existingTeam != null) {
                existingTeam.setTeamName(t.getTeamName());
                existingTeam.setTeamMembers(t.getTeamMembers());
                existingTeam.setRawPassword(t.getPassword());
                return saveTeamAndUpdateCache(existingTeam);
            } else {
                return saveTeamAndUpdateCache(t);
            }
        } catch (Exception err) {
            return null;
        }
    }
    private Team saveTeamAndUpdateCache(Team team) {
        try {
            redisProvider.getRedisTemplate().opsForValue()
                    .set(redisProvider.wrapperTeamGetData(team.getTeamUsername()),
                            redisProvider.getObjectMapper().writeValueAsString(team));
            return teamRepository.save(team);
        } catch (Exception err) {
            return null;
        }
    }


    @Override
    public Team create(Team entity) {
        return  saveTeamAndUpdateCache(entity);
    }

    @Override
    public void remove(Team t) {
        try {
            teamRepository.delete(t);
            removeTeamFromCache(t);
        } catch (Exception err) {
            // Tangani kesalahan jika diperlukan
        }
    }

    private void removeTeamFromCache(Team team) {
        try {
            redisProvider.getRedisTemplate().delete(redisProvider.wrapperTeamGetData(team.getTeamUsername()));
        } catch (Exception err) {
            // Tangani kesalahan jika diperlukan
        }
    }



}
