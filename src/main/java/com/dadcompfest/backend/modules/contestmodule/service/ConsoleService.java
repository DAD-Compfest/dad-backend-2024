package com.dadcompfest.backend.modules.contestmodule.service;

import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOContestConsole;

public interface ConsoleService {
   Object executeCommand(String contestName, DTOContestConsole dtoContestConsole);

}
