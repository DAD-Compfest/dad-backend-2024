package com.dadcompfest.backend.modules.submissionmodule.model;


import com.dadcompfest.backend.modules.authmodule.model.Team;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
public class Contest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contest_id")
    private Long contestId;

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
    private List<Team> teams;
}


