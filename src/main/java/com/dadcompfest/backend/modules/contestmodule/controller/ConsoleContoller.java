package com.dadcompfest.backend.modules.contestmodule.controller;

import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOContestConsole;
import com.dadcompfest.backend.modules.contestmodule.service.ConsoleService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/contest/{contestName}/console")
public class ConsoleContoller {

    @Autowired
    private ConsoleService consoleService;

    @PostMapping("/execute")
    public ResponseEntity<Object> executeCommand(
            @PathVariable String contestName,
            @RequestBody DTOContestConsole dtoContestConsole
    ) {
        Map<String,Object> data = new HashMap<>();
        try {
            Object result = consoleService.executeCommand(contestName, dtoContestConsole);
            return ResponseEntity.ok(result);
        } catch (BadRequestException | BadSqlGrammarException e) {
            data.put("code", HttpStatus.BAD_REQUEST.value());
            data.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data);
        } catch (Exception e) {
            data.put("code", HttpStatus.BAD_REQUEST.value());
            data.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
        }
    }
}
