package com.dadcompfest.backend.modules.contestmodule.service;

import com.dadcompfest.backend.modules.contestmodule.model.Question;
import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOQuestionCreation;

import java.util.List;

public interface QuestionService {
    void createQuestion(DTOQuestionCreation question);
    List<Question> getQuestionsByContestId(String contestId);
}
