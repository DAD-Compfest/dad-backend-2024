package com.dadcompfest.backend.modules.contestmodule.model;


import com.dadcompfest.backend.modules.authmodule.model.Team;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Contest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "contest_id", updatable = false, nullable = false)
    private UUID contestId;
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

    @ElementCollection
    @MapKeyJoinColumn(name = "team_username")
    @Column(name = "banned")
    private Map<String, Boolean> bannedTeams;
}



