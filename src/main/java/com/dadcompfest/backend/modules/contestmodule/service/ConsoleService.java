package com.dadcompfest.backend.modules.contestmodule.service;

import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOContestConsole;
import org.apache.coyote.BadRequestException;

public interface ConsoleService {
   Object executeCommand(String contestName, DTOContestConsole dtoContestConsole) throws BadRequestException;

}
