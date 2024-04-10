package com.dadcompfest.backend.modules.contestmodule.adapter;
import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.modules.contestmodule.adapter.ContestAdapter;
import com.dadcompfest.backend.modules.contestmodule.model.Contest;
import com.dadcompfest.backend.modules.contestmodule.model.ContestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Setter
@Getter
@RedisHash(value = "redis_contest", timeToLive = 360)
@NoArgsConstructor
public class RedisContest implements ContestAdapter, Serializable {
    @Id
    @AccessType(AccessType.Type.PROPERTY)
    String contestId;
    @Indexed
    private String contestName;
    private String description;
    private String startTime;
    private String endTime;
    private String teams;

    public RedisContest(Contest contest) {
        this.contestId = contest.getContestId();
        this.contestName = contest.getContestName();
        this.description = contest.getDescription();
        this.startTime = contest.getStartTime();
        this.endTime = contest.getEndTime();
        setTeams(contest.getTeams());
    }

    public Map<String,Team> getTeams(){
        try {
            return new ObjectMapper().readValue(teams, new TypeReference<Map<String,Team>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTeams(Map<String,Team> teams){
        try {
            this.teams = new ObjectMapper().writeValueAsString(teams);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Contest getContest() {
        return new ContestBuilder().setContestId(contestId).
                setContestName(contestName)
                .setDescription(description)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setTeams(getTeams()).build();
    }

    @Override
    public String getContestId() {
        return contestId;
    }
}
