package com.dadcompfest.backend.modules.contestmodule.service;

import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOContestConsole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.Selection;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConsoleServiceImpl implements ConsoleService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    private static final Logger logger = LoggerFactory.getLogger(ConsoleServiceImpl.class);

    @Override
    @Transactional
    public Object executeCommand(String contestName, DTOContestConsole dtoContestConsole) throws BadRequestException {
        String schemaName = "contest_" + contestName.toLowerCase().replaceAll("\\s+", "_");
        String command = dtoContestConsole.getCommand().toLowerCase();
        List<String> commandSplit = List.of(command.split(";"));

        try {
            String modifiedCommand = modifyCommandWithSchema(command, schemaName);

            if (command.contains("select") && commandSplit.size() > 1) {
                throw new BadRequestException("Operasi SELECT tidak boleh ditulis dengan multi query!");
            } else {
                Object result;
                if (command.contains("select") && commandSplit.size() == 1) {
                    result = jdbcTemplate.queryForList(modifiedCommand);
                } else {
                    jdbcTemplate.execute(modifiedCommand);
                    result = "Berhasil melakukan query";
                }
                return result;
            }
        } catch (BadSqlGrammarException e) {
            logger.error("Error executing command: {}", e.getMessage());
            throw new BadSqlGrammarException("", "Kesalahan dalam syntax SQL", e.getSQLException());
        } catch (BadRequestException e) {
            logger.error("Error executing command: {}", e.getMessage());
            throw new BadRequestException("Kesalahan dalam syntax SQL");
        } catch (Exception e) {
            logger.error("Unexpected error occurred: {}", e.getMessage(), e);
            return "Unexpected error occurred: " + e.getMessage();
        }
    }

    private String modifyCommandWithSchema(String command, String schemaName) {
        String modifiedCommand = command;
        modifiedCommand = modifiedCommand.replaceFirst("(?i)create\\s+table\\s+", "CREATE TABLE " + schemaName + ".");
        modifiedCommand = modifiedCommand.replaceFirst("(?i)alter\\s+table\\s+", "ALTER TABLE " + schemaName + ".");
        modifiedCommand = modifiedCommand.replaceFirst("(?i)drop\\s+table\\s+", "DROP TABLE " + schemaName + ".");
        modifiedCommand = modifiedCommand.replaceFirst("(?i)create\\s+index\\s+", "CREATE INDEX " + schemaName + ".");
        modifiedCommand = modifiedCommand.replaceFirst("(?i)drop\\s+index\\s+", "DROP INDEX " + schemaName + ".");
        modifiedCommand = modifiedCommand.replaceFirst("(?i)insert\\s+into\\s+", "INSERT INTO " + schemaName + ".");
        modifiedCommand = modifiedCommand.replaceFirst("(?i)update\\s+", "UPDATE " + schemaName + ".");
        modifiedCommand = modifiedCommand.replaceFirst("(?i)delete\\s+from\\s+", "DELETE FROM " + schemaName + ".");
        modifiedCommand = modifiedCommand.replaceFirst("(?i)select\\s+", "SELECT ");
        modifiedCommand = modifiedCommand.replaceAll("(?i)from\\s+([^\\s]+)", "FROM " + schemaName + ".$1");
        return modifiedCommand;
    }

}

