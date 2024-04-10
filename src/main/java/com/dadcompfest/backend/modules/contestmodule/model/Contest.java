package com.dadcompfest.backend.modules.contestmodule.model;


import com.dadcompfest.backend.modules.authmodule.model.Team;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class Contest implements Serializable {
    @Id
    @Column(name = "contest_id")
    private String contestId;

    private String contestName;
    private String description;
    private String startTime;
    private String endTime;

    @ManyToMany
    @JoinTable(
            name = "contest_team",
            joinColumns = @JoinColumn(name = "contest_id"),
            inverseJoinColumns = @JoinColumn(name = "team_username", referencedColumnName = "team_username"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"contest_id", "team_username"})}
    )
    @MapKeyJoinColumn(name = "team_username")
    private Map<String,Team> teams;
}



