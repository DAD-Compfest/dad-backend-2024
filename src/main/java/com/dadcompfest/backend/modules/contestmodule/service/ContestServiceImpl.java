package com.dadcompfest.backend.modules.contestmodule.service;
import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.modules.contestmodule.model.Contest;
import com.dadcompfest.backend.modules.contestmodule.model.Question;
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
        contest.getTeams().put(contestId, team);
        return contest;
    }
    @Override
    public Contest getContestById(String contestId) {
        UUID uuid = UUID.fromString(contestId);
        return contestRepository.findById(uuid).orElseGet(()-> null);
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


}
