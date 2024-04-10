package com.dadcompfest.backend.modules.contestmodule.repository;

import com.dadcompfest.backend.modules.contestmodule.adapter.RedisContest;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.CrudRepository;

public interface RedisContestRepository extends KeyValueRepository<RedisContest, String> {
}
