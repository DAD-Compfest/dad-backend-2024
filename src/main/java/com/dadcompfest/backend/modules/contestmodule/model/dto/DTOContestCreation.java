package com.dadcompfest.backend.modules.contestmodule.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DTOContestCreation {
    private String contestName;
    private  String description;
    private String startTime;
    private String endTime;
}
