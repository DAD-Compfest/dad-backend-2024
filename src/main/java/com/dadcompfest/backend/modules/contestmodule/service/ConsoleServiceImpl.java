package com.dadcompfest.backend.modules.contestmodule.service;

import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOContestConsole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsoleServiceImpl implements ConsoleService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Object executeCommand(String contestName, DTOContestConsole dtoContestConsole) {
        String schemaName = "contest_" + contestName.toLowerCase().replaceAll("\\s+", "_");
        String command = dtoContestConsole.getCommand().toLowerCase();

        List<String> commandSplit = List.of(command.split(";"));

        jdbcTemplate.execute("SET SEARCH_PATH TO " + schemaName);

        if(command.contains("select") && commandSplit.size() > 1) {

            return "Operasi SELECT tidak boleh ditulis dengan multi query!";

        }else if(command.contains("select") && commandSplit.size() == 1) {
            try {
                return jdbcTemplate.queryForList(command);
            } catch (Exception e) {
                return "Error executing command: " + e.getMessage();
            }
        }else {
            try {
                jdbcTemplate.execute(command);
                return "Berhasil melakukan query";
            } catch (Exception e) {
                return "Error executing command: " + e.getMessage();
            }
        }
    }
}
