package com.dadcompfest.backend.modules.contestmodule.controller;

import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.modules.contestmodule.adapter.RedisContest;
import com.dadcompfest.backend.modules.contestmodule.model.Contest;
import com.dadcompfest.backend.modules.contestmodule.repository.RedisContestRepository;
import com.dadcompfest.backend.modules.contestmodule.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.javafaker.Faker;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contest")
public class ContestController {
    @Autowired
    ContestService contestService;

    @Autowired
    RedisContestRepository redisContestRepository;

    @GetMapping("/seed/delete-seed-contest")
    public String deleteSeedContest(){
        redisContestRepository.deleteAll();
        return "seeds have been deleted";
    }

    @GetMapping("/seed/seed-join-contest")
    public  String seedTeam(){
        Iterable<RedisContest> redisContestIterable = redisContestRepository.findAll();
        List<RedisContest> redisContestList = new ArrayList<>();
        redisContestIterable.forEach(redisContestList::add);

        for(RedisContest contest : redisContestList){
            for (int i = 0; i < 20; i++){
                Team team = new Team();

                team.setTeamUsername(UUID.randomUUID().toString());
                contestService.joinContest(contest.getContestId(), team);
            }
        }
        return  "OK";
    }
    @GetMapping("/seed/seed-contest")
    public String seedContest() {
        Faker faker = new Faker();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        for (int i = 0; i < 1; i++) {
            Contest contest = new Contest();
            contest.setContestId(UUID.randomUUID().toString());
            contest.setContestName("TEST-CONTEST-" + faker.company().name() + i);
            contest.setTeams(new HashMap<>());
            // Set start time
            LocalDateTime startTime = LocalDateTime.now(); // or any other time you desire
            contest.setStartTime(startTime.format(formatter));
            // Set end time
            LocalDateTime endTime = startTime.plusDays(7); // for example, end time is 7 days after start time
            contest.setEndTime(endTime.format(formatter));
            contest.setDescription("fake contest only for test purpose");
            // Do something with contest object, perhaps persist it to a database or store it somewhere
            contestService.createContest(contest);
        }
        return "successfully seeding contests";
    }
}

