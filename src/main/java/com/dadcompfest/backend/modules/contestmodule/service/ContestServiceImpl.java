package com.dadcompfest.backend.modules.contestmodule.service;
import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.modules.contestmodule.model.Contest;
import com.dadcompfest.backend.modules.contestmodule.adapter.RedisContest;
import com.dadcompfest.backend.modules.contestmodule.repository.ContestRepository;
import com.dadcompfest.backend.modules.contestmodule.repository.RedisContestRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ContestServiceImpl implements ContestService{

    @Autowired
    private ContestRepository postgresContestRepository;
    @Autowired
    private RedisContestRepository redisContestRepository;
    private static final Logger logger = LogManager.getLogger(ContestServiceImpl.class);


    @Override
    public Contest createContest(Contest contest) {
        RedisContest redisContest = new RedisContest(contest);
        return redisContestRepository.save(redisContest).getContest();
    }
    @Override
    public Contest joinContest(String contestId, Team team) {
        Contest contest = getContestById(contestId);
        contest.getTeams().put(team.getTeamUsername(), team);
        RedisContest redisContest = new RedisContest(contest);
        redisContestRepository.save(redisContest);
        return contest;
    }
    @Override
    public Contest getContestById(String contestId) {
        RedisContest redisContest = redisContestRepository.findById(contestId).orElseGet(()->null);
        if(redisContest != null){
            logger.info("cached");
            return  redisContest.getContest();
        }
        return postgresContestRepository.findById(contestId).orElseGet(()->null);
    }
}
