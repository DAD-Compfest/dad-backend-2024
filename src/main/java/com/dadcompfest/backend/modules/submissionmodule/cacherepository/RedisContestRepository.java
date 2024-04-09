package com.dadcompfest.backend.modules.submissionmodule.cacherepository;

import com.dadcompfest.backend.modules.submissionmodule.model.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedisContestRepository extends JpaRepository<Contest, Long> {
}
