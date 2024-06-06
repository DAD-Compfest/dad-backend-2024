package com.dadcompfest.backend.modules.contestmodule.repository;
import com.dadcompfest.backend.modules.contestmodule.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT q FROM Question q WHERE q.contest.contestId = :contestId")
    List<Question> findByContestId(@Param("contestId") UUID contestId);
}
