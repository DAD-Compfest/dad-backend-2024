package com.dadcompfest.backend.modules.contestmodule.controller;

import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOContestConsole;
import com.dadcompfest.backend.modules.contestmodule.service.ConsoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contest/{contestName}/console")
public class ConsoleContoller {

    @Autowired
    private ConsoleService consoleService;

    @PostMapping("/execute")
    public ResponseEntity<String> executeCommand(
            @PathVariable String contestName,
            @RequestBody DTOContestConsole dtoContestConsole
    ) {
        String result = consoleService.executeCommand(contestName, dtoContestConsole);
        return ResponseEntity.ok(result);
    }
}
