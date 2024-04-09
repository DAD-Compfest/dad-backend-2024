package com.dadcompfest.backend.modules.contestmodule.repository;

import com.dadcompfest.backend.modules.contestmodule.adapter.RedisContest;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface RedisContestRepository extends KeyValueRepository<RedisContest, String> {
}
