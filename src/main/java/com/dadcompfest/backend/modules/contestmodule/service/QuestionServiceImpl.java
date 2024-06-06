package com.dadcompfest.backend.modules.contestmodule.service;


import com.dadcompfest.backend.modules.contestmodule.model.Question;
import com.dadcompfest.backend.modules.contestmodule.model.QuestionBuilder;
import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOQuestionCreation;
import com.dadcompfest.backend.modules.contestmodule.repository.ContestRepository;
import com.dadcompfest.backend.modules.contestmodule.repository.QuestionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuestionServiceImpl implements QuestionService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public void createQuestion(DTOQuestionCreation question) {
        entityManager.createNativeQuery
                        ("INSERT INTO Question " +
                                "(question_number, question, answer, point_provided, contest_id) " +
                                "VALUES (?, ?, ?, ?, ?)")
                .setParameter(1, question.getQuestionNumber())
                .setParameter(2, question.getQuestion())
                .setParameter(3, question.getAnswer())
                .setParameter(4, question.getPointProvided())
                .setParameter(5, question.getContestId())
                .executeUpdate();
    }

    @Override
    public List<Question> getQuestionsByContestId(String contestId) {
        return questionRepository.findByContestId(UUID.fromString(contestId));
    }
}
