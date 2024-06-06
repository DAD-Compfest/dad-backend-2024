package com.dadcompfest.backend.modules.contestmodule.service;
import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.modules.contestmodule.model.Contest;
import com.dadcompfest.backend.modules.contestmodule.repository.ContestRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class ContestServiceImpl implements ContestService{

    @Autowired
    private ContestRepository contestRepository;




    @Override
    public Contest createContest(Contest contest) {
        return contestRepository.save(contest);
    }
    @Override
    public Contest joinContest(String contestId, Team team) {
        Contest contest = getContestById(contestId);
        contest.getTeams().put(contestId, team);
        return contest;
    }
    @Override
    public Contest getContestById(String contestId) {
        UUID uuid = UUID.fromString(contestId);
        return contestRepository.findById(uuid).orElseGet(()-> null);
    }

    @Override
    public List<Contest> getAll() {
        return contestRepository.findAll();
    }
}
