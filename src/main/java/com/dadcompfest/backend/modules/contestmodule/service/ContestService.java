package com.dadcompfest.backend.modules.contestmodule.service;

import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.modules.contestmodule.model.Contest;

public interface ContestService {
    Contest createContest(Contest contest);
    Contest joinContest(String contestId, Team team);
    Contest getContestById(String contestId);
}
