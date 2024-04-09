package com.dadcompfest.backend.modules.submissionmodule.cacherepository;
import com.dadcompfest.backend.modules.submissionmodule.model.Question;
import org.springframework.data.repository.CrudRepository;


public interface RedisQuestionRepository extends CrudRepository<Question, Long> {
}
