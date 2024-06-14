package com.dadcompfest.backend.modules.contestmodule.service;

import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOContestConsole;

public interface ConsoleService {
   String executeCommand(String contestName, DTOContestConsole dtoContestConsole);

}
