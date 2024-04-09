package com.dadcompfest.backend.modules.contestmodule.model;

import com.dadcompfest.backend.modules.authmodule.model.Team;

import java.util.List;
import java.util.Map;

public class ContestBuilder {
    private String contestId;
    private String contestName;
    private String description;
    private String startTime;
    private String endTime;
    private Map<String,Team> teams;

    public ContestBuilder setContestId(String contestId) {
        this.contestId = contestId;
        return this;
    }

    public ContestBuilder setContestName(String contestName) {
        this.contestName = contestName;
        return this;
    }

    public ContestBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public ContestBuilder setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public ContestBuilder setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public ContestBuilder setTeams(Map<String, Team> teams) {
        this.teams = teams;
        return this;
    }

    public Contest build() {
        Contest contest = new Contest();
        contest.setContestId(this.contestId);
        contest.setContestName(this.contestName);
        contest.setDescription(this.description);
        contest.setStartTime(this.startTime);
        contest.setEndTime(this.endTime);
        contest.setTeams(this.teams);
        return contest;
    }
}
