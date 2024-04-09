package com.dadcompfest.backend.modules.contestmodule.adapter;
import com.dadcompfest.backend.modules.contestmodule.adapter.ContestAdapter;
import com.dadcompfest.backend.modules.contestmodule.model.Contest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@Setter
@Getter
@RedisHash(value = "redis_contest")
public class RedisContest implements ContestAdapter {
    Contest contest;
    @Id
    @AccessType(AccessType.Type.PROPERTY)
    String contestId;

    public RedisContest(Contest contest){
        this.contest = contest;
        this.contestId = contest.getContestId();
    }

    public RedisContest(){

    }

    @Override
    public Contest getContest() {
        return contest;
    }

    @Override
    public String getContestId() {
        return contestId;
    }
}
