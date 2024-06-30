package com.dadcompfest.backend.modules.contestmodule.controller;


import com.dadcompfest.backend.common.util.ResponseHandler;
import com.dadcompfest.backend.modules.authmodule.model.Team;
import com.dadcompfest.backend.modules.authmodule.service.UserService;
import com.dadcompfest.backend.modules.contestmodule.model.Contest;
import com.dadcompfest.backend.modules.contestmodule.model.ContestBuilder;
import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOContestCreation;
import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOContestModification;
import com.dadcompfest.backend.modules.contestmodule.model.dto.DTOTeamJoinContest;
import com.dadcompfest.backend.modules.contestmodule.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/contest")
public class ContestController {
    @Autowired
    ContestService contestService;
    @Autowired 
    UserService<Team> teamService;

    
    @PostMapping("/join-contest")
    public String joinContest(@RequestBody DTOTeamJoinContest body) {
        Team team = teamService.findByUsername(body.getTeamUsername());
        contestService.joinContest(body.getContestId(), team);
        return "OK";
    }
    

    @PostMapping("/create")
    public String addContest(@RequestBody DTOContestCreation body){
        System.out.println(body);
        contestService.createContest(new ContestBuilder()
                .setContestName(body.getContestName())
                .setDescription(body.getDescription())
                .setStartTime(body.getStartTime())
                .setEndTime(body.getEndTime())
                .build());
        return "OK";
    }

    @PostMapping("/edit")
    public String editContestData(@RequestBody DTOContestModification body){
        Contest contest = contestService.getContestById(body.getContestId());
        if(contest != null){
            contest.setContestName(body.getContestName());
            contest.setStartTime(body.getStartTime());
            contest.setEndTime(body.getStartTime());
            contest.setDescription(body.getDescription());
            contestService.createContest(contest);
        }
        return "OK";
    }

    @GetMapping("/data/{id}")
    public ResponseEntity<Object> getContestById(@PathVariable String id){
        Map<String, Object> data = new HashMap<>();
        System.out.println("ANJENGGG "+id);
        data.put("message", "Berhasil mendapat data contest");
        data.put("contest", contestService.getContestById(id));
        return ResponseHandler.generateResponse("Berhasil mendapat data contest",
                HttpStatus.ACCEPTED, data);
    };

    @GetMapping("/all")
    public ResponseEntity<Object> getAllContest(){
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Berhasil mendapatkan data semua contest");
        data.put("contests", contestService.getAll());
        return ResponseHandler.generateResponse("Berhasil mendapatkan data semua contest",
                HttpStatus.ACCEPTED, data);
    }

    @DeleteMapping("/delete/{contestId}")
    public ResponseEntity<Object> deleteContest(@PathVariable String contestId){
        contestService.deleteContest(contestId);
        return ResponseEntity.ok().build();
    }

}

