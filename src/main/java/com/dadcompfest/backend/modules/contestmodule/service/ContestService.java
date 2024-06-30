package com.dadcompfest.backend.modules.contestmodule.service;

import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.modules.contestmodule.model.Contest;
import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOBanTeam;

import java.util.List;

public interface ContestService {
    Contest createContest(Contest contest);
    Contest joinContest(String contestId, Team team);
    Contest getContestById(String contestId);
    void deleteContest(String contestId);
    List<Contest> getAll();
    void banTeam(DTOBanTeam bannedTeamInfo);
    void unbanTeam(DTOBanTeam unbannedTeamInfo);
    void kickTeam(DTOBanTeam kickTeamInfo);
}
