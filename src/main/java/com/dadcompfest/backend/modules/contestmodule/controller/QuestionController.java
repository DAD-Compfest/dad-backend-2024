package com.dadcompfest.backend.modules.contestmodule.controller;

import com.dadcompfest.backend.modules.contestmodule.model.Question;
import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOQuestionCreation;
import com.dadcompfest.backend.modules.contestmodule.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/contest/{contestId}")
    public ResponseEntity<List<Question>> getQuestionsByContestId(@PathVariable String contestId) {
        List<Question> questions = questionService.getQuestionsByContestId(contestId);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createQuestion(@RequestBody DTOQuestionCreation questionDTO) {
        questionService.createQuestion(questionDTO);
        return ResponseEntity.ok().build();
    }
}
