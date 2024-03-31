package com.dadcompfest.backend.modules.authmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dadcompfest.backend.modules.authmodule.model.Team;



public interface TeamRepository  extends JpaRepository<Team, Long>{

    @Query("SELECT u FROM Team u WHERE u.teamUsername = :username")
    Team findByUsername(@Param("username") String username);
    @Query("DELETE FROM Team u WHERE u.teamUsername = :username")
    void deleteByUsername(@Param("username") String username);

}
