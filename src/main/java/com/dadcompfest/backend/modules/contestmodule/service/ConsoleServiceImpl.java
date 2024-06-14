package com.dadcompfest.backend.modules.contestmodule.service;

import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOContestConsole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConsoleServiceImpl implements ConsoleService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String executeCommand(String contestName, DTOContestConsole dtoContestConsole) {
        String schemaName = "contest_" + contestName.toLowerCase().replaceAll("\\s+", "_");
        String command = dtoContestConsole.getCommand();

        try {
            jdbcTemplate.execute("SET SEARCH_PATH TO " + schemaName);

            jdbcTemplate.execute(command);

            return "command executed successfully in schema: " + schemaName;
        } catch (Exception e) {
            return "Error executing DDL command: " + e.getMessage();
        }
    }
}
