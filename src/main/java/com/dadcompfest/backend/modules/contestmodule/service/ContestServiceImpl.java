package com.dadcompfest.backend.modules.contestmodule.service;
import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.modules.contestmodule.model.Contest;
import com.dadcompfest.backend.modules.contestmodule.model.Question;
import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOBanTeam;
import com.dadcompfest.backend.modules.contestmodule.repository.ContestRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class ContestServiceImpl implements ContestService{

    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private DataSource dataSource;
    @PersistenceContext
    private EntityManager entityManager;



    @Override
    public Contest createContest(Contest contest) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            String schemaName = "contest_" + contest.getContestName().toLowerCase().replaceAll("\\s+", "_");
            statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + schemaName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contestRepository.save(contest);
    }
    @Override
    public Contest joinContest(String contestId, Team team) {
        Contest contest = getContestById(contestId);
        contest.getTeams().put(team.getTeamUsername(), team);
        return contest;
    }
    @Override
    public Contest getContestById(String contestId) {
        try{
            UUID uuid = UUID.fromString(contestId);
            System.out.println(contestId);
            return contestRepository.findById(uuid).orElseGet(()-> null);
        }
        catch(Exception err){
            System.out.println(err.getMessage());
            return null;
        }
    }

    @Override
    public void deleteContest(String contestId) {
        entityManager.createNativeQuery("DELETE FROM Question WHERE contest_id = (?)")
                            .setParameter(1, UUID.fromString(contestId))
                            .executeUpdate();
        contestRepository.deleteById(UUID.fromString(contestId));
    }

    @Override
    public List<Contest> getAll() {
        return contestRepository.findAll();
    }
    @Override
    public void banTeam(DTOBanTeam bannedTeamInfo) {
        Contest contest = getContestById(bannedTeamInfo.getContestId());
        if(contest != null){
            contest.getBannedTeams().put(bannedTeamInfo.getTeamUsername(), true);
            contestRepository.save(contest);
        }
    }
    @Override
    public void unbanTeam(DTOBanTeam unbannedTeamInfo) {
        Contest contest = getContestById(unbannedTeamInfo.getContestId());
        if(contest != null){
            contest.getBannedTeams().remove(unbannedTeamInfo.getTeamUsername());
            contestRepository.save(contest);
        }
    }

    @Override
    public void kickTeam(DTOBanTeam kickTeamInfo) {
        Contest contest = getContestById(kickTeamInfo.getContestId());
        if(contest != null){
            contest.getTeams().remove(kickTeamInfo.getTeamUsername());
            contestRepository.save(contest);
        }
    }


}
