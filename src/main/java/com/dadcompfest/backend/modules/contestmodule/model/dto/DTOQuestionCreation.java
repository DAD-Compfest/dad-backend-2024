package com.dadcompfest.backend.modules.contestmodule.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DTOQuestionCreation {
    private Long questionNumber;
    private String question;
    private String answer;
    private int pointProvided;
    private String questionImage;
    private String contestId;
}
