package com.dadcompfest.backend.modules.contestmodule.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"contest_id", "question_number"}))
public class Question implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;
    @Column(name = "question_number")
    private  Long questionNumber;
    private  String question;
    private  String answer;
    private  int pointProvided;
    @Column(nullable = true) // This makes questionImage nullable
    private String questionImage;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    @JsonIgnore
    private Contest contest;
    public Question() {

    }
}
