package com.dadcompfest.backend.modules.contestmodule.service;

import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOContestConsole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsoleServiceImpl implements ConsoleService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Object executeCommand(String contestName, DTOContestConsole dtoContestConsole) {
        String schemaName = "contest_" + contestName.toLowerCase().replaceAll("\\s+", "_");
        String command = dtoContestConsole.getCommand().toLowerCase();
        Logger logger = LoggerFactory.getLogger(ConsoleServiceImpl.class);

        List<String> commandSplit = List.of(command.split(";"));

        try {
            if (command.contains("select") && commandSplit.size() > 1) {
                return "Operasi SELECT tidak boleh ditulis dengan multi query!";
            } else {
                entityManager.createNativeQuery("SET SEARCH_PATH TO " + schemaName).executeUpdate();

                if (command.contains("select") && commandSplit.size() == 1) {
                    return entityManager.createNativeQuery(command).getResultList();
                } else {
                    entityManager.createNativeQuery(command).executeUpdate();
                    return "Berhasil melakukan query";
                }
            }
        } catch (PersistenceException e) {
            logger.error("Error executing command: {}", e.getMessage());
            return "Tidak bisa melakukan query";
        } catch (Exception e) {
            logger.error("Unexpected error occurred: {}", e.getMessage(), e);
            return "Unexpected error occurred: " + e.getMessage();
        }
    }
}
