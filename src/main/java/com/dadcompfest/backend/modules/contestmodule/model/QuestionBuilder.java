package com.dadcompfest.backend.modules.contestmodule.model;

public class QuestionBuilder {
    private Long questionNumber;
    private String question;
    private String answer;
    private int pointProvided;
    private Contest contest;

    public QuestionBuilder questionNumber(Long questionNumber) {
        this.questionNumber = questionNumber;
        return this;
    }

    public QuestionBuilder question(String question) {
        this.question = question;
        return this;
    }

    public QuestionBuilder answer(String answer) {
        this.answer = answer;
        return this;
    }

    public QuestionBuilder pointProvided(int pointProvided) {
        this.pointProvided = pointProvided;
        return this;
    }

    public QuestionBuilder contest(Contest contest) {
        this.contest = contest;
        return this;
    }

    public Question build() {
        Question question = new Question();
        question.setQuestionNumber(this.questionNumber);
        question.setQuestion(this.question);
        question.setAnswer(this.answer);
        question.setPointProvided(this.pointProvided);
        question.setContest(this.contest);
        return question;
    }
}