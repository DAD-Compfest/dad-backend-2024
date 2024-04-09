package com.dadcompfest.backend.modules.contestmodule.provider;

import com.dadcompfest.backend.common.enums.RedisConstants;
import com.dadcompfest.backend.common.provider.RedisProvider;
import com.dadcompfest.backend.modules.contestmodule.model.Contest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Getter
@Component
public class RedisProviderContestExtension {
    @Autowired
    private RedisProvider redisProvider;
    @Autowired
    private RedisTemplate<String, Contest> redisContest;

    public String getContestRedisPrefix(){
        return RedisConstants.DATA.getPrefix()+RedisConstants.CONTEST.getPrefix();
    }
}
