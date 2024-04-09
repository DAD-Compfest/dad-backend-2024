package com.dadcompfest.backend.modules.contestmodule.repository;
import com.dadcompfest.backend.modules.contestmodule.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
