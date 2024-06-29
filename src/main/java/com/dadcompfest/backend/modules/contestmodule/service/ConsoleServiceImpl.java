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
        String queryTableName = String.format(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = '%s'"
                ,schemaName
            );
        String querySchemaName = "SELECT schema_name, schema_owner\n" +
                "FROM information_schema.schemata;";

        List<Map<String, Object>> ListSchemaName = jdbcTemplate.queryForList(querySchemaName);

        for(Map<String, Object> schema : ListSchemaName){
            Object nameSchemaNow = schema.get("schema_name");
            if(command.contains(nameSchemaNow.toString()) && !nameSchemaNow.equals(schemaName)){
                throw new BadRequestException("Access to schema or command not allowed.");
            }
        }

        if (command.contains("information_schema")) {
            throw new BadRequestException("Access to schema or command not allowed.");
        }

        if(command.contains("table")){
            command = command.replaceAll("(?i)\\btable\\b\\s+(\\w+)", "TABLE " + schemaName + ".$1");
        }

        List<Map<String, Object>> tableName = jdbcTemplate.queryForList(queryTableName);

        for(Map<String, Object> table : tableName){
            command = command.replaceAll(
                    String.format("(?i)\\b%s\\b", table.get("table_name")),
                    schemaName + "." + table.get("table_name"));
        }

        List<String> commandSplit = List.of(command.split(";"));

        try {
            if (command.contains("select") && commandSplit.size() > 1) {
                throw new BadRequestException("Operasi SELECT tidak boleh ditulis dengan multi query!");
            } else {
                Object result;
                if (command.contains("select") && commandSplit.size() == 1) {
                    result = jdbcTemplate.queryForList(commandSplit.getFirst());
                } else {
                    for(int i = 0; i < commandSplit.size(); i++){
                        jdbcTemplate.execute(commandSplit.get(i));

                        if(commandSplit.get(i).contains("create")){
                            String tableNameNew = command.split(
                                    schemaName + "\\.")[1].split("\\s+|\\(")[0];
                            command = command.replaceAll(
                                    String.format("(?i)\\b%s\\b", tableNameNew),
                                    schemaName + "." + tableNameNew);
                            commandSplit = List.of(command.split(";"));
                        }
                    }
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

}

