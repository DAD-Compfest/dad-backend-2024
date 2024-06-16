package com.dadcompfest.backend.modules.contestmodule.service;


import com.dadcompfest.backend.modules.contestmodule.model.Contest;
import com.dadcompfest.backend.modules.contestmodule.model.Question;
import com.dadcompfest.backend.modules.contestmodule.model.QuestionBuilder;
import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOQuestionCreation;
import com.dadcompfest.backend.modules.contestmodule.repository.ContestRepository;
import com.dadcompfest.backend.modules.contestmodule.repository.QuestionRepository;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public void createQuestion(DTOQuestionCreation question) {
        entityManager.createNativeQuery
                        ("INSERT INTO Question " +
                                "(question_number, question, answer, point_provided, contest_id, question_image) " +
                                "VALUES (?, ?, ?, ?, ?, ?)")
                .setParameter(1, question.getQuestionNumber())
                .setParameter(2, question.getQuestion())
                .setParameter(3, question.getAnswer())
                .setParameter(4, question.getPointProvided())
                .setParameter(5, UUID.fromString(question.getContestId()))
                .setParameter(6, question.getQuestionImage())
                .executeUpdate();
    }

    @Override
    public void editQuestion(Long questionId, DTOQuestionCreation question) {
        entityManager.createNativeQuery("UPDATE Question SET" +
                        " question_number=(?)," +
                        " question=(?)," +
                        " answer=(?)," +
                        " point_provided=(?)," +
                        " question_image=(?)" +
                        " WHERE question_id=(?) AND contest_id=(?)")
                .setParameter(1, question.getQuestionNumber())
                .setParameter(2, question.getQuestion())
                .setParameter(3, question.getAnswer())
                .setParameter(4, question.getPointProvided())
                .setParameter(5, question.getQuestionImage())
                .setParameter(6, questionId)
                .setParameter(7, UUID.fromString(question.getContestId()))
                .executeUpdate();
    }

    @Override
    public void deleteQuestion(Long questionId) {
        questionRepository.deleteById(questionId);
    }

    @Override
    public List<Question> getQuestionsByContestId(String contestId) {
        return questionRepository.findByContestId(UUID.fromString(contestId));
    }
}
