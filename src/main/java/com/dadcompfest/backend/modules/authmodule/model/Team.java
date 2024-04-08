package com.dadcompfest.backend.modules.authmodule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "team_members")
    @Convert(converter = StringToListConverter.class)
    private List<String> teamMembers;

    public Team(){}

    public void setPassword(String password){
        this.password = AuthProvider.getInstance().encode(password);
    }
    @JsonSetter("password")
    public  void setRawPassword(String password){
        this.password = password;
    }


    @Converter
    static class StringToListConverter implements AttributeConverter<List<String>, String> {

        @Override
        public String convertToDatabaseColumn(List<String> list) {
            if(list == null) return "";
            return String.join(",", list);
        }

        @Override
        public List<String> convertToEntityAttribute(String joined) {
            if(joined == null) return new ArrayList<>();
            return new ArrayList<>(Arrays.asList(joined.split(",")));
        }
    }

}

