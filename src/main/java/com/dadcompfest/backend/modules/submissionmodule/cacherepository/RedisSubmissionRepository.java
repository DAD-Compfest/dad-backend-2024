package com.dadcompfest.backend.modules.submissionmodule.cacherepository;

import com.dadcompfest.backend.modules.submissionmodule.model.Submission;
import org.springframework.data.repository.CrudRepository;

public interface RedisSubmissionRepository extends CrudRepository<Submission, Long> {
}
