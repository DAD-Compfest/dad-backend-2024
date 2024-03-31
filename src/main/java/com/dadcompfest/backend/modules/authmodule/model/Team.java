package com.dadcompfest.backend.modules.authmodule.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;

import com.dadcompfest.backend.modules.authmodule.provider.AuthProvider;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
@Setter
@Entity
public class Team{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long teamId;

    private String teamName;
    
    @Column(unique = true, name = "team_username")
    private String teamUsername;
    
    @Column(unique = true)
    private String teamEmail;

    private String password;
    
    private String teamMembers;

    public Team(){}

    public void setPassword(String password){
        this.password = AuthProvider.getInstance().encode(password);
    }
    public  void setRawPassword(String password){
        this.password = password;
    }

  
    public List<String> getTeamMembersList() {
        if (teamMembers == null || teamMembers.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(teamMembers.split(","));
    }

    public void setTeamMembers(List<String> teamMembers) {
        this.teamMembers = String.join(",", teamMembers);
    }
}

