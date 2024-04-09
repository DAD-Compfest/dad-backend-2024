package com.dadcompfest.backend.modules.submissionmodule.cacherepository;

import com.dadcompfest.backend.modules.submissionmodule.model.ContestPoint;
import org.springframework.data.repository.CrudRepository;

public interface RedisContestPointRepository extends CrudRepository<ContestPoint, Long> {
}
